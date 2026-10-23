package com.feedback.servlet;

import com.feedback.dao.FeedbackDAO;
import com.feedback.model.User;
import com.feedback.util.Flash;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {
    private static final int MAX_LEN = 2000;
    private final FeedbackDAO dao = new FeedbackDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Flash.consume(req);
        try {
            req.setAttribute("feedback", dao.findByUserId(user.getId()));
        } catch (SQLException e) {
            getServletContext().log("Load feedback error", e);
            req.setAttribute("msgType", "error");
            req.setAttribute("msg", "Could not load your feedback.");
        }
        req.getRequestDispatcher("/WEB-INF/views/feedback.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String mode = req.getParameter("mode");
        String text = req.getParameter("text") == null ? "" : req.getParameter("text").trim();

        if (text.isEmpty()) {
            Flash.set(req, "error", "Feedback cannot be empty.");
        } else if (text.length() > MAX_LEN) {
            Flash.set(req, "error", "Feedback is too long (max " + MAX_LEN + " characters).");
        } else {
            try {
                boolean exists = dao.findByUserId(user.getId()) != null;
                if ("add".equals(mode)) {
                    if (exists) {
                        Flash.set(req, "error", "You already submitted feedback. Use Edit Feedback.");
                    } else {
                        dao.insert(user.getId(), text);
                        Flash.set(req, "success", "Feedback submitted. Thank you!");
                    }
                } else if ("edit".equals(mode)) {
                    if (!exists) {
                        Flash.set(req, "error", "No feedback to edit yet. Add one first.");
                    } else {
                        dao.updateByUser(user.getId(), text);
                        Flash.set(req, "success", "Feedback updated.");
                    }
                } else {
                    Flash.set(req, "error", "Invalid request.");
                }
            } catch (SQLException e) {
                getServletContext().log("Save feedback error", e);
                Flash.set(req, "error", "Something went wrong. Please try again.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/feedback");
    }
}
