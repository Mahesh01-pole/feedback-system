package com.feedback.dao;

import com.feedback.model.Feedback;
import com.feedback.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    private static final String BASE =
            "SELECT f.id, f.user_id, u.username, u.email, f.feedback_text, f.created_date, f.updated_date "
          + "FROM feedback f JOIN users u ON u.id = f.user_id ";

    private Feedback map(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setId(rs.getInt("id"));
        f.setUserId(rs.getInt("user_id"));
        f.setUsername(rs.getString("username"));
        f.setEmail(rs.getString("email"));
        f.setText(rs.getString("feedback_text"));
        f.setCreatedDate(rs.getDate("created_date"));
        f.setUpdatedDate(rs.getDate("updated_date"));
        return f;
    }

    public Feedback findByUserId(int userId) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(BASE + "WHERE f.user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Feedback> findAll() throws SQLException {
        List<Feedback> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(BASE + "ORDER BY f.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void insert(int userId, String text) throws SQLException {
        String sql = "INSERT INTO feedback (user_id, feedback_text, created_date, updated_date) "
                   + "VALUES (?, ?, CURDATE(), CURDATE())";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, text);
            ps.executeUpdate();
        }
    }

    public void updateByUser(int userId, String text) throws SQLException {
        String sql = "UPDATE feedback SET feedback_text = ?, updated_date = CURDATE() WHERE user_id = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, text);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void updateById(int id, String text) throws SQLException {
        String sql = "UPDATE feedback SET feedback_text = ?, updated_date = CURDATE() WHERE id = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, text);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM feedback WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
