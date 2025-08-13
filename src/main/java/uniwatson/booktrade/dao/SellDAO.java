package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.SellItem;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellDAO {

    public List<SellItem> listBySeller(String sellerId) throws SQLException {
        String sql = "SELECT s.Seller_ID,s.Book,s.Price,b.Name,b.Author " +
                     "FROM sell s JOIN book b ON s.Book=b.ISBN WHERE s.Seller_ID=?";
        List<SellItem> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SellItem si = new SellItem();
                    si.setSellerId(rs.getString("Seller_ID"));
                    si.setIsbn(rs.getString("Book"));
                    si.setPrice(rs.getInt("Price"));
                    si.setBookName(rs.getString("Name"));
                    si.setAuthor(rs.getString("Author"));
                    list.add(si);
                }
            }
        }
        return list;
    }

    public List<SellItem> listByIsbn(String isbn) throws SQLException {
        String sql = "SELECT s.Seller_ID,s.Book,s.Price,b.Name,b.Author " +
                     "FROM sell s JOIN book b ON s.Book=b.ISBN WHERE s.Book=?";
        List<SellItem> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SellItem si = new SellItem();
                    si.setSellerId(rs.getString("Seller_ID"));
                    si.setIsbn(rs.getString("Book"));
                    si.setPrice(rs.getInt("Price"));
                    si.setBookName(rs.getString("Name"));
                    si.setAuthor(rs.getString("Author"));
                    list.add(si);
                }
            }
        }
        return list;
    }

    /** 上架：若 book 不存在會自動建立；同賣家同 ISBN 會更新價格 */
    public void add(String sellerId, String isbn, int price, String name, String author) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                // 建立 book（若不存在）
                boolean exists;
                try (PreparedStatement chk = c.prepareStatement("SELECT 1 FROM book WHERE ISBN=?")) {
                    chk.setString(1, isbn);
                    try (ResultSet rs = chk.executeQuery()) { exists = rs.next(); }
                }
                if (!exists) {
                    try (PreparedStatement ins = c.prepareStatement(
                            "INSERT INTO book (ISBN, Cover_img, Name, Author, Publish_year, Original_price, Category, Description, ImageURL) " +
                            "VALUES (?, 'default.jpg', ?, ?, NULL, NULL, NULL, NULL, NULL)")) {
                        ins.setString(1, isbn);
                        ins.setString(2, (name == null || name.isBlank()) ? isbn : name.trim());
                        ins.setString(3, (author == null) ? null : author.trim());
                        ins.executeUpdate();
                    }
                }

                // 建立/更新賣場
                try (PreparedStatement up = c.prepareStatement(
                        "INSERT INTO sell (Seller_ID, Book, Price) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE Price=VALUES(Price)")) {
                    up.setString(1, sellerId);
                    up.setString(2, isbn);
                    up.setInt(3, price);
                    up.executeUpdate();
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /** 下架/售出：刪賣場，並嘗試清理孤兒書目 */
    public void remove(String sellerId, String isbn) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(
                        "DELETE FROM sell WHERE Seller_ID=? AND Book=?")) {
                    ps.setString(1, sellerId);
                    ps.setString(2, isbn);
                    ps.executeUpdate();
                }
                cleanupOrphanBook(c, isbn);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /** 無賣家、無課程引用、無任何訂單紀錄 → 才刪除 book */
    private void cleanupOrphanBook(Connection c, String isbn) throws SQLException {
        // 仍有其他賣家？
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM sell WHERE Book=? LIMIT 1")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        // 被課程引用？
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM course WHERE Used_book=? LIMIT 1")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        // 有訂單紀錄？（order 是保留字，表名要加反引號）
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM `order` WHERE Book=? LIMIT 1")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        // 以上皆無，才安全刪除 book
        try (PreparedStatement del = c.prepareStatement("DELETE FROM book WHERE ISBN=?")) {
            del.setString(1, isbn);
            del.executeUpdate();
        }
    }
}
