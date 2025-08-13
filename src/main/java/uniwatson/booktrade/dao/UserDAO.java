package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Member;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {

    /** 新增使用者（符合你的 member 資料表） */
    public void createUser(Member m) throws SQLException {
        if (m.getToken() == null || m.getToken().isBlank()) {
            m.setToken(UUID.randomUUID().toString());
        }

        String sql = "INSERT INTO member (USER_ID, Name, Token, PhoneNum, Department, Password) " +
                     "VALUES (?,?,?,?,?,?)";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getMemberId());   // ★ 用 getMemberId
            ps.setString(2, m.getName());
            ps.setString(3, m.getToken());
            ps.setString(4, m.getPhoneNum());
            ps.setString(5, m.getDepartment());
            ps.setString(6, m.getPassword());
            ps.executeUpdate();
        }
    }

    /** 若其他舊程式呼叫 insert(...)，導到 createUser(...) */
    public void insert(Member m) throws SQLException {
        createUser(m);
    }
}
