package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Violation;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViolationDAO {
    public List<Violation> listAll() throws SQLException {
        String sql = "SELECT * FROM violation_record ORDER BY Serial_Num DESC";
        List<Violation> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean add(String userId, int managerId, String reason) throws SQLException {
        String sql = "INSERT INTO violation_record (Time, Reason, User_ID, Manager_ID) VALUES (NOW(), ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setString(2, userId);
            ps.setInt(3, managerId);
            return ps.executeUpdate() == 1;
        }
    }

    private Violation map(ResultSet rs) throws SQLException {
        Violation v = new Violation();
        v.setSerialNum(rs.getInt("Serial_Num"));
        v.setTime(rs.getTimestamp("Time"));
        v.setReason(rs.getString("Reason"));
        v.setUserId(rs.getString("User_ID"));
        v.setManagerId(rs.getInt("Manager_ID"));
        return v;
    }
}
