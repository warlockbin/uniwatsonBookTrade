package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.MessageDAO;
import uniwatson.booktrade.models.Member;
import uniwatson.booktrade.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

    // ===== GET：顯示訊息，或從詳情頁啟動對話 =====
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        String action   = trim(req.getParameter("action"));
        String orderStr = trim(req.getParameter("orderId"));

        // 從書籍詳情「聯絡賣家」啟動對話：/messages?action=start&sellerId=...&isbn=...
        if ("start".equalsIgnoreCase(action)) {
            String sellerId = trim(req.getParameter("sellerId"));
            String isbn     = trim(req.getParameter("isbn"));
            if (sellerId == null || isbn == null) {
                resp.sendRedirect(req.getContextPath() + "/books?action=view&isbn=" + (isbn==null?"":isbn));
                return;
            }

            try (Connection c = DBUtil.getConnection()) {
                c.setAutoCommit(false);
                int orderId = ensureOpenOrder(c, me.getMemberId(), sellerId, isbn);
                c.commit();

                resp.sendRedirect(req.getContextPath() + "/messages?orderId=" + orderId);
                return;
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        // 一般顯示對話：必須有 orderId
        if (orderStr == null || orderStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        int orderId;
        try { orderId = Integer.parseInt(orderStr); }
        catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            req.setAttribute("messages", new MessageDAO().listByOrder(orderId));
            req.setAttribute("orderId", orderId);
            req.getRequestDispatcher("/messages.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ===== POST：送出訊息；若沒帶 orderId（從詳情開聊），就先建/取訂單再送 =====
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        String orderStr = trim(req.getParameter("orderId"));
        String sellerId = trim(req.getParameter("sellerId")); // 詳情頁可能會帶
        String isbn     = trim(req.getParameter("isbn"));     // 詳情頁可能會帶
        String to       = trim(req.getParameter("to"));
        String content  = trim(req.getParameter("content"));
        if (content == null || content.isEmpty()) {
            // 空內容就回到列表（或原對話頁）
            if (orderStr != null && !orderStr.isBlank())
                resp.sendRedirect(req.getContextPath() + "/messages?orderId=" + orderStr);
            else
                resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);

            int orderId;
            if (orderStr == null || orderStr.isBlank()) {
                // 沒有 orderId，代表從詳情頁開聊：先確保/建立訂單
                if (sellerId == null || isbn == null) {
                    c.rollback();
                    resp.sendRedirect(req.getContextPath() + "/orders");
                    return;
                }
                orderId = ensureOpenOrder(c, me.getMemberId(), sellerId, isbn);
            } else {
                orderId = Integer.parseInt(orderStr);
            }

            // 自動決定收件人：對方 = (我 == 賣家 ? 買家 : 賣家)
            Party p = getOrderParties(c, orderId);
            if (p == null) { c.rollback(); resp.sendRedirect(req.getContextPath()+"/orders"); return; }
            String meId = me.getMemberId();
            String receiver = (to != null && !to.isBlank())
                    ? to
                    : (meId.equals(p.sellerId) ? p.buyerId : p.sellerId);

            // 寫入訊息
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO message(`Time`,`Content`,`SendoutID`,`ReceivedID`,`OrderID`) " +
                    "VALUES (NOW(), ?, ?, ?, ?)")) {
                ps.setString(1, content);
                ps.setString(2, meId);
                ps.setString(3, receiver);
                ps.setInt(4, orderId);
                ps.executeUpdate();
            }

            c.commit();
            resp.sendRedirect(req.getContextPath() + "/messages?orderId=" + orderId);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ========= helpers =========
    private static String trim(String s){ return s==null? null : s.trim(); }

    /** 找到現有未完成的訂單；沒有就建立一筆，回傳 orderId。未完成以 Finish_date IS NULL 判定，最穩。 */
    private int ensureOpenOrder(Connection c, String buyerId, String sellerId, String isbn) throws SQLException {
        // 先找現有【未完成】（Finish_date IS NULL）
        Integer found = null;
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT ID FROM `order` WHERE Seller_ID=? AND Buyer_ID=? AND Book=? " +
                "AND Finish_date IS NULL ORDER BY ID DESC LIMIT 1")) {
            ps.setString(1, sellerId);
            ps.setString(2, buyerId);
            ps.setString(3, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) found = rs.getInt(1);
            }
        }
        if (found != null) return found;

        // 沒有就新建一筆（Status=0 表示處理中；你的 JSP 目前會把 !=1 視為處理中）
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO `order`(Seller_ID, Buyer_ID, Book, Establish_date, Status) " +
                "VALUES (?,?,?,NOW(),0)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sellerId);
            ps.setString(2, buyerId);
            ps.setString(3, isbn);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to create order for chat");
    }

    /** 取出訂單雙方 Id */
    private Party getOrderParties(Connection c, int orderId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT Seller_ID, Buyer_ID FROM `order` WHERE ID=?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Party p = new Party();
                    p.sellerId = rs.getString(1);
                    p.buyerId  = rs.getString(2);
                    return p;
                }
            }
        }
        return null;
    }

    private static class Party {
        String sellerId;
        String buyerId;
    }
}
