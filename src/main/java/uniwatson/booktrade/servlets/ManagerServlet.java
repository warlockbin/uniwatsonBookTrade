package uniwatson.booktrade.servlets;

import uniwatson.booktrade.models.Member;
import uniwatson.booktrade.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/manager")
public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 權限檢查：需已登入且角色為 manager
        Member me = (Member) req.getSession().getAttribute("user");
        String role = (String) req.getSession().getAttribute("role");
        if (me == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"manager".equals(role)) {
            resp.sendError(403, "需要管理者權限");
            return;
        }

        // 簡單的儀表板統計數據：會員數、書籍數、上架數、訂單(處理中/完成)
        try (Connection c = DBUtil.getConnection()) {

            req.setAttribute("memberCount", scalarCount(c, "SELECT COUNT(*) FROM member"));
            req.setAttribute("bookCount",   scalarCount(c, "SELECT COUNT(*) FROM book"));
            req.setAttribute("sellCount",   scalarCount(c, "SELECT COUNT(*) FROM sell"));
            req.setAttribute("orderPending",scalarCount(c, "SELECT COUNT(*) FROM `order` WHERE Status=0"));
            req.setAttribute("orderDone",   scalarCount(c, "SELECT COUNT(*) FROM `order` WHERE Status=1"));

            // 導向管理後台頁
            req.getRequestDispatcher("/managerDashboard.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // 若之後有管理操作（例如封禁、刪書…），可在這裡處理
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    private int scalarCount(Connection c, String sql) throws SQLException {
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
