package com.feedback.servlet;

import com.feedback.dao.FeedbackDAO;
import com.feedback.util.Flash;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/** Admin CRUD. "View" is handled client-side from the listed data (Read). */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final int MAX_LEN = 2000;
    private final FeedbackDAO dao = new FeedbackDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Flash.consume(req);
        try {
            req.setAttribute("feedbackList", dao.findAll());
        } catch (SQLException e) {
            getServletContext().log("Admin list error", e);
            req.setAttribute("msgType", "error");
            req.setAttribute("msg", "Could not load feedback.");
        }
        req.getRequestDispatcher("/WEB-INF/views/adminDashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            Flash.set(req, "error", "Invalid feedback id.");
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }
        try {
            if ("delete".equals(action)) {
                dao.deleteById(id);
                Flash.set(req, "success", "Feedback deleted.");
            } else if ("edit".equals(action)) {
                String text = req.getParameter("text") == null ? "" : req.getParameter("text").trim();
                if (text.isEmpty() || text.length() > MAX_LEN) {
                    Flash.set(req, "error", "Feedback must be 1 to " + MAX_LEN + " characters.");
                } else {
                    dao.updateById(id, text);
                    Flash.set(req, "success", "Feedback updated.");
                }
            } else {
                Flash.set(req, "error", "Invalid action.");
            }
        } catch (SQLException e) {
            getServletContext().log("Admin action error", e);
            Flash.set(req, "error", "Something went wrong. Please try again.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
