package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.OrderDAO;
import uniwatson.booktrade.models.Member;
import uniwatson.booktrade.models.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/orders"})
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        try {
            OrderDAO dao = new OrderDAO();
            // 你的 Member 欄位是 MemberId
            String myId = me.getMemberId();

            List<Order> buyList  = dao.listByBuyer(myId);
            List<Order> sellList = dao.listBySeller(myId);

            req.setAttribute("buyOrders",  buyList);
            req.setAttribute("sellOrders", sellList);
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        String op = req.getParameter("op");
        if (op == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            OrderDAO dao = new OrderDAO();
            String myId = me.getMemberId();

            switch (op) {
                case "delete": {
                    // 僅允許刪除「屬於自己（買家或賣家）」的訂單；DAO 會先刪 message 再刪 order
                    String idStr = req.getParameter("id");
                    if (idStr != null && !idStr.isBlank()) {
                        int id = Integer.parseInt(idStr);
                        dao.deleteByIdAndOwner(id, myId);
                    }
                    break;
                }
                // 如果你的「購買此書」是送到 /orders
                case "buy": {
                    // 可選：若你已有其他 Servlet 實作下單，這段不會動到資料。
                    // 這裡僅保護性 redirect，避免 404。
                    break;
                }
                default:
                    break;
            }

            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
