package uniwatson.booktrade.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DEF_HOST = "127.0.0.1";
    private static final String DEF_PORT = "3306";
    private static final String DEF_DB   = "uniwatson_book_trade";
    private static final String DEF_USER = "root";
    private static final String DEF_PASS = "";

    public static Connection getConnection() throws SQLException {
        // 優先讀完整 JDBC_URL；沒有再組合 host/port/db
        String jdbc = getenv("JDBC_URL", null);
        if (jdbc == null || jdbc.isBlank()) {
            String host = getenv("DB_HOST", DEF_HOST);
            String port = getenv("DB_PORT", DEF_PORT);
            String db   = getenv("DB_NAME", DEF_DB);
            String params = getenv("DB_PARAMS",
                "useUnicode=true&characterEncoding=utf8&useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            jdbc = String.format("jdbc:mysql://%s:%s/%s?%s", host, port, db, params);
        }
        String user = getenv("DB_USER", DEF_USER);
        String pass = getenv("DB_PASS", DEF_PASS);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignore) {}

        return DriverManager.getConnection(jdbc, user, pass);
    }

    private static String getenv(String k, String defv){
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? defv : v;
    }
}
