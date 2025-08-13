package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Order;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // 依買家查
    public List<Order> listByBuyer(String buyerId) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE Buyer_ID=? ORDER BY ID DESC";
        List<Order> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // 依賣家查
    public List<Order> listBySeller(String sellerId) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE Seller_ID=? ORDER BY ID DESC";
        List<Order> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // 取單筆
    public Order getById(int id) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE ID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    // 將最新「處理中(1)」的訂單設為完成(2)
    public int completeNewestPending(String sellerId, String isbn) throws SQLException {
        String sql = "UPDATE `order` SET Status=2, Finish_date=NOW() " +
                     "WHERE ID=(SELECT id2 FROM (" +
                     "  SELECT ID AS id2 FROM `order` " +
                     "  WHERE Seller_ID=? AND Book=? AND Status=1 " +
                     "  ORDER BY ID DESC LIMIT 1" +
                     ") t)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sellerId);
            ps.setString(2, isbn);
            return ps.executeUpdate();
        }
    }

    // 刪除：只能刪自己的訂單；先刪留言避免 FK 例外
    public boolean deleteByIdAndOwner(int id, String ownerId) throws SQLException {
        String delMsg = "DELETE FROM message WHERE OrderID=?";
        String delOrd = "DELETE FROM `order` WHERE ID=? AND (Seller_ID=? OR Buyer_ID=?)";
        Connection c = null;
        try {
            c = DBUtil.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement ps1 = c.prepareStatement(delMsg)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            int rows;
            try (PreparedStatement ps2 = c.prepareStatement(delOrd)) {
                ps2.setInt(1, id);
                ps2.setString(2, ownerId);
                ps2.setString(3, ownerId);
                rows = ps2.executeUpdate();
            }

            c.commit();
            return rows > 0;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignore) {}
            throw e;
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignore) {}
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("ID"));
        o.setSellerId(rs.getString("Seller_ID"));
        o.setBuyerId(rs.getString("Buyer_ID"));
        o.setBook(rs.getString("Book"));
        try { o.setEstablishDate(rs.getTimestamp("Establish_date")); } catch (SQLException ignore) {}
        try { o.setStatus(rs.getInt("Status")); } catch (SQLException ignore) {}
        try { o.setPhoneNum(rs.getString("PhoneNum")); } catch (SQLException ignore) {}
        try { o.setFinishDate(rs.getTimestamp("Finish_date")); } catch (SQLException ignore) {}
        try { o.setPrice(rs.getInt("Price")); } catch (SQLException ignore) {}
        return o;
    }
}
