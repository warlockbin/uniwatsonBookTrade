package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.OrderDAO;
import uniwatson.booktrade.dao.SellDAO;
import uniwatson.booktrade.models.Member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sell")
public class SellServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        try {
            req.setAttribute("mySell", new SellDAO().listBySeller(me.getMemberId()));
            req.getRequestDispatcher("/myItems.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Member me = (Member) req.getSession().getAttribute("user");
        if (me == null) { resp.sendRedirect("login.jsp"); return; }

        String op   = req.getParameter("op");
        String isbn = req.getParameter("isbn");

        try {
            if ("add".equals(op)) {
                int price     = Integer.parseInt(req.getParameter("price"));
                String name   = req.getParameter("name");
                String author = req.getParameter("author");
                new SellDAO().add(me.getMemberId(), isbn, price, name, author);
            } else if ("remove".equals(op)) {
                new SellDAO().remove(me.getMemberId(), isbn);
            } else if ("sold".equals(op)) {
                // 先把最新待處理的訂單標記為完成，再移除上架
                new OrderDAO().completeNewestPending(me.getMemberId(), isbn);
                new SellDAO().remove(me.getMemberId(), isbn);
            }
            resp.sendRedirect(req.getContextPath() + "/sell");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
