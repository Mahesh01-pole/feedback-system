package com.feedback.model;

import java.sql.Date;

public class Feedback {
    private int id;
    private int userId;
    private String username;
    private String email;
    private String text;
    private Date createdDate;
    private Date updatedDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }
}
