package com.feedback.dao;

import com.feedback.model.User;
import com.feedback.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, email, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                return u;
            }
        }
    }

    public boolean existsByUsername(String username) throws SQLException {
        return exists("SELECT 1 FROM users WHERE username = ?", username);
    }

    public boolean existsByEmail(String email) throws SQLException {
        return exists("SELECT 1 FROM users WHERE email = ?", email);
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Inserts the user and returns the generated id. */
    public int create(User u) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getRole());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }
}
