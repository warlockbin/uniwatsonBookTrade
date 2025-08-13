package uniwatson.booktrade.dao;

import uniwatson.booktrade.models.Course;
import uniwatson.booktrade.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public List<Course> listAll() throws SQLException {
        String sql = "SELECT c.Course_ID, c.Used_book, c.Professor, c.Course_name, b.Name AS BookName " +
                     "FROM course c LEFT JOIN book b ON c.Used_book=b.ISBN ORDER BY c.Course_ID";
        List<Course> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course co = new Course();
                co.setCourseId(rs.getInt("Course_ID"));
                co.setIsbn(rs.getString("Used_book"));
                co.setProfessor(rs.getString("Professor"));
                co.setName(rs.getString("Course_name"));
                co.setBookName(rs.getString("BookName"));
                list.add(co);
            }
        }
        return list;
    }
}
