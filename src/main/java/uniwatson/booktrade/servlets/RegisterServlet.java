package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.UserDAO;
import uniwatson.booktrade.models.Member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId = trim(req.getParameter("userId"));     // 表單欄位
        String name   = trim(req.getParameter("name"));
        String phone  = trim(req.getParameter("phone"));
        String dept   = trim(req.getParameter("department"));
        String pass   = trim(req.getParameter("password"));

        if (blank(userId) || blank(name) || blank(phone) || blank(pass)) {
            req.setAttribute("error", "請完整填寫必填欄位");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        Member m = new Member();
        m.setMemberId(userId);           // ★ 用 setMemberId
        m.setName(name);
        m.setPhoneNum(phone);
        m.setDepartment(dept);
        m.setPassword(pass);
        m.setToken(UUID.randomUUID().toString()); // 避免 Token NOT NULL 失敗

        try {
            new UserDAO().createUser(m);
            HttpSession session = req.getSession();
            session.setAttribute("user", m);
            session.setAttribute("role", "user");
            resp.sendRedirect(req.getContextPath() + "/home.jsp");
        } catch (SQLIntegrityConstraintViolationException dup) {
            req.setAttribute("error", "此帳號已存在，請改用其他帳號");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s){ return s==null? null : s.trim(); }
    private static boolean blank(String s){ return s==null || s.trim().isEmpty(); }
}
