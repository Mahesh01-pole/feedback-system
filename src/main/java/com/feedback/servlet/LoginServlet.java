package com.feedback.servlet;

import com.feedback.dao.UserDAO;
import com.feedback.model.User;
import com.feedback.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("user") != null) {
            redirectHome(req, resp, (User) s.getAttribute("user"));
            return;
        }
        if ("1".equals(req.getParameter("registered"))) {
            req.setAttribute("success", "Registration successful. Please log in.");
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username") == null ? "" : req.getParameter("username").trim();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");

        if (username.isEmpty() || password.isEmpty()) {
            fail(req, resp, "Username and password are required.");
            return;
        }
        try {
            User u = userDAO.findByUsername(username);
            if (u == null || !PasswordUtil.verify(password, u.getPasswordHash())) {
                fail(req, resp, "Invalid username or password.");
                return;
            }
            u.setPasswordHash(null); // never keep the hash in the session
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate(); // prevent session fixation
            req.getSession(true).setAttribute("user", u);
            redirectHome(req, resp, u);
        } catch (SQLException e) {
            getServletContext().log("Login DB error", e);
            fail(req, resp, "Something went wrong. Please try again.");
        }
    }

    private void fail(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    private void redirectHome(HttpServletRequest req, HttpServletResponse resp, User u) throws IOException {
        String target = "ADMIN".equals(u.getRole()) ? "/admin/dashboard" : "/feedback";
        resp.sendRedirect(req.getContextPath() + target);
    }
}
