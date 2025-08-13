package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.CourseDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/courses")
public class CourseServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("courses", new CourseDAO().listAll());
            req.getRequestDispatcher("courses.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
