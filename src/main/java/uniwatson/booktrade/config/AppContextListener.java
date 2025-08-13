package uniwatson.booktrade.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 如需全域預設設定可放這裡，目前不需要
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1) 關掉 MySQL 清理執行緒，避免 memory leak 警告
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignore) {
        }

        // 2) 只註銷本 WebApp classloader 載入的 JDBC Driver
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            if (d.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(d);
                } catch (Exception ignore) { }
            }
        }
    }
}
