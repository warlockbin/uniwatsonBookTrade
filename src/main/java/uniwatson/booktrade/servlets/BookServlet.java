package uniwatson.booktrade.servlets;

import uniwatson.booktrade.dao.BookDAO;
import uniwatson.booktrade.dao.SellDAO;
import uniwatson.booktrade.models.Book;
import uniwatson.booktrade.models.SellItem;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = trim(req.getParameter("action"));
        String isbn   = trim(req.getParameter("isbn"));

        try {
            // === 詳情頁 ===
            if ("view".equalsIgnoreCase(action) && isbn != null && !isbn.isEmpty()) {
                BookDAO bookDAO = new BookDAO();
                SellDAO sellDAO = new SellDAO();

                Book book = bookDAO.getByIsbn(isbn);
                List<SellItem> sellers = sellDAO.listByIsbn(isbn);

                // 找不到書也不丟 404 —— 建一個只有 ISBN 的暫存物件，讓 JSP 能正常顯示
                if (book == null) {
                    book = new Book();
                    book.setIsbn(isbn);
                    book.setName("（尚無書名資料）");
                }

                req.setAttribute("book", book);
                req.setAttribute("sellers", sellers);
                req.getRequestDispatcher("/bookDetail.jsp").forward(req, resp);
                return;
            }

            // === 搜尋列表（預設） ===
            String q = trim(req.getParameter("q"));
            List<Book> list = new BookDAO().search(q == null ? "" : q);
            req.setAttribute("books", list);
            req.setAttribute("q", q == null ? "" : q);
            req.getRequestDispatcher("/bookList.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
