package uniwatson.booktrade.servlets;

import uniwatson.booktrade.models.Member;
import uniwatson.booktrade.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 顯示登入頁
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String userId = trim(req.getParameter("userId"));
        String pass   = trim(req.getParameter("password"));

        if (blank(userId) || blank(pass)) {
            req.setAttribute("error", "請輸入帳號與密碼");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try (Connection c = DBUtil.getConnection()) {
            // 1) 查詢會員
            Member m = null;
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT USER_ID, Name, Token, PhoneNum, Department, Password " +
                    "FROM member WHERE USER_ID = ?")) {
                ps.setString(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String dbPass = rs.getString("Password");
                        if (pass.equals(dbPass)) { // 目前明碼；之後可替換為雜湊
                            m = new Member();
                            m.setMemberId(rs.getString("USER_ID"));
                            m.setName(rs.getString("Name"));
                            m.setToken(rs.getString("Token"));
                            m.setPhoneNum(rs.getString("PhoneNum"));
                            m.setDepartment(rs.getString("Department"));
                            m.setPassword(dbPass);
                        }
                    }
                }
            }

            if (m == null) {
                req.setAttribute("error", "帳號或密碼錯誤");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // 2) 判斷是否為管理者
            String role = "user";
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM manager WHERE USER_ID=? LIMIT 1")) {
                ps.setString(1, m.getMemberId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) role = "manager";
                }
            }

            // 3) 放入 session 並導回首頁
            HttpSession session = req.getSession(true);
            session.setAttribute("user", m);
            session.setAttribute("role", role);
            resp.sendRedirect(req.getContextPath() + "/home.jsp");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s){ return s==null? null : s.trim(); }
    private static boolean blank(String s){ return s==null || s.trim().isEmpty(); }
}
