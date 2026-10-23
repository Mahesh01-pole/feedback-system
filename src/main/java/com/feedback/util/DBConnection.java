package com.feedback.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("db.properties not found on classpath");
            PROPS.load(in);
            Class.forName(PROPS.getProperty("driver", "com.mysql.cj.jdbc.Driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() {}

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("url"),
                PROPS.getProperty("username"),
                PROPS.getProperty("password"));
    }
}
