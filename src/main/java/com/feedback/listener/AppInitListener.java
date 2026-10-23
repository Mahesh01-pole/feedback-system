package com.feedback.listener;

import com.feedback.dao.UserDAO;
import com.feedback.model.User;
import com.feedback.util.DBConnection;
import com.feedback.util.PasswordUtil;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

/** Creates tables and seeds the admin + demo data on first start. */
@WebListener
public class AppInitListener implements ServletContextListener {

    private static final String USERS_DDL =
            "CREATE TABLE IF NOT EXISTS users ("
          + "id INT AUTO_INCREMENT PRIMARY KEY,"
          + "username VARCHAR(50) NOT NULL UNIQUE,"
          + "email VARCHAR(100) NOT NULL UNIQUE,"
          + "password_hash VARCHAR(100) NOT NULL,"
          + "role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER')";

    private static final String FEEDBACK_DDL =
            "CREATE TABLE IF NOT EXISTS feedback ("
          + "id INT AUTO_INCREMENT PRIMARY KEY,"
          + "user_id INT NOT NULL UNIQUE,"
          + "feedback_text TEXT NOT NULL,"
          + "created_date DATE NOT NULL,"
          + "updated_date DATE NOT NULL,"
          + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try (Connection c = DBConnection.get(); Statement st = c.createStatement()) {
            st.executeUpdate(USERS_DDL);
            st.executeUpdate(FEEDBACK_DDL);

            UserDAO users = new UserDAO();
            if (users.findByUsername("admin") == null) {
                users.create(new User("admin", "admin@example.com", PasswordUtil.hash("admin123"), "ADMIN"));

                int u1 = users.create(new User("demo1", "demo1@example.com", PasswordUtil.hash("demo123"), "USER"));
                int u2 = users.create(new User("demo2", "demo2@example.com", PasswordUtil.hash("demo123"), "USER"));

                String sql = "INSERT INTO feedback (user_id, feedback_text, created_date, updated_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, u1);
                    ps.setString(2, "This is a great product! Highly recommended.");
                    ps.setDate(3, Date.valueOf("2024-08-09"));
                    ps.setDate(4, Date.valueOf("2024-08-09"));
                    ps.executeUpdate();

                    ps.setInt(1, u2);
                    ps.setString(2, "I had some issues with the service, but customer support was helpful.");
                    ps.setDate(3, Date.valueOf("2024-08-08"));
                    ps.setDate(4, Date.valueOf("2024-08-08"));
                    ps.executeUpdate();
                }
            }
            event.getServletContext().log("Feedback System: database ready.");
        } catch (Exception e) {
            event.getServletContext().log(
                "Feedback System: DATABASE INIT FAILED - check MySQL is running and db.properties credentials.", e);
        }
    }
}
