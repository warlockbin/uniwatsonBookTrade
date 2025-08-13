package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Message;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> listByOrder(int orderId) throws SQLException {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE OrderID=? ORDER BY Time ASC, Serial_Num ASC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void send(int orderId, String from, String to, String content) throws SQLException {
        String sql = "INSERT INTO message (Time, Content, SendoutID, ReceivedID, OrderID) " +
                     "VALUES (NOW(), ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setString(2, from);
            ps.setString(3, to);
            ps.setInt(4, orderId);
            ps.executeUpdate();
        }
    }

    private Message map(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setSerialNum(rs.getInt("Serial_Num"));
        m.setTime(rs.getTimestamp("Time"));
        m.setContent(rs.getString("Content"));
        m.setSendoutId(rs.getString("SendoutID"));
        m.setReceivedId(rs.getString("ReceivedID"));
        m.setOrderId(rs.getInt("OrderID"));
        return m;
    }
}
