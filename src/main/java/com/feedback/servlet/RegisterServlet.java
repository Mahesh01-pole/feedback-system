package com.feedback.servlet;

import com.feedback.dao.UserDAO;
import com.feedback.model.User;
import com.feedback.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String confirm = req.getParameter("confirmPassword") == null ? "" : req.getParameter("confirmPassword");

        String error = null;
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            error = "All fields are required.";
        } else if (username.length() < 3 || username.length() > 50) {
            error = "Username must be 3 to 50 characters.";
        } else if (!EMAIL.matcher(email).matches()) {
            error = "Please enter a valid email address.";
        } else if (password.length() < 6) {
            error = "Password must be at least 6 characters.";
        } else if (!password.equals(confirm)) {
            error = "Passwords do not match.";
        }

        try {
            if (error == null && userDAO.existsByUsername(username)) error = "Username already taken.";
            if (error == null && userDAO.existsByEmail(email)) error = "Email already registered.";
            if (error == null) {
                userDAO.create(new User(username, email, PasswordUtil.hash(password), "USER"));
                resp.sendRedirect(req.getContextPath() + "/login?registered=1");
                return;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            error = "Username or email already exists.";
        } catch (SQLException e) {
            getServletContext().log("Register DB error", e);
            error = "Something went wrong. Please try again.";
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
}
