package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Book;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    /** 依 ISBN 取單本（詳情頁用） */
    public Book getByIsbn(String isbn) throws SQLException {
        String sql = "SELECT ISBN, Name, Author, Publish_year, Original_price, " +
                     "Category, Description, ImageURL, Cover_img " +
                     "FROM book WHERE ISBN = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    /** 關鍵字搜尋（列表頁） */
    public List<Book> search(String q) throws SQLException {
        boolean empty = (q == null || q.isBlank());
        String base = "SELECT ISBN, Name, Author, Publish_year, Original_price, " +
                      "Category, Description, ImageURL, Cover_img FROM book ";
        String sql = empty
                ? base + "ORDER BY Name ASC, ISBN ASC"
                : base + "WHERE ISBN = ? OR Name LIKE ? OR Author LIKE ? " +
                         "ORDER BY (ISBN = ?) DESC, Name ASC";

        List<Book> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (!empty) {
                String like = "%" + q.trim() + "%";
                ps.setString(1, q.trim());
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, q.trim()); // 讓 ISBN 完全匹配的排前面
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    /** 若允許自由上架時自動補書目，可先確保 book 表有一筆基本資料 */
    public void ensureBook(String isbn, String name, String author) throws SQLException {
        if (getByIsbn(isbn) != null) return;
        String sql = "INSERT INTO book(ISBN, Name, Author) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setString(2, name);
            ps.setString(3, author);
            ps.executeUpdate();
        }
    }

    // ---------------- private helpers ----------------

    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setIsbn(rs.getString("ISBN"));

        try { b.setName(rs.getString("Name")); } catch (Exception ignore) {}
        try { b.setAuthor(rs.getString("Author")); } catch (Exception ignore) {}
        try { b.setPublishYear(rs.getInt("Publish_year")); } catch (Exception ignore) {}

        // 修正：Original_price 用 BigDecimal，對應 Book.setOriginalPrice(BigDecimal)
        try { b.setOriginalPrice(rs.getBigDecimal("Original_price")); } catch (Exception ignore) {}

        try { b.setCategory(rs.getString("Category")); } catch (Exception ignore) {}
        try { b.setDescription(rs.getString("Description")); } catch (Exception ignore) {}

        // 修正：方法名多半是 setImageUrl（小寫 l），不是 setImageURL
        try { b.setImageUrl(rs.getString("ImageURL")); } catch (Exception ignore) {}

        try { b.setCoverImg(rs.getString("Cover_img")); } catch (Exception ignore) {}

        return b;
    }
}
