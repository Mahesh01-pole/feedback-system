package com.feedback.filter;

import com.feedback.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/feedback", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) rq;
        HttpServletResponse resp = (HttpServletResponse) rs;

        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        String ctx = req.getContextPath();
        String path = req.getServletPath();

        if (user == null) {
            resp.sendRedirect(ctx + "/login");
            return;
        }
        boolean admin = "ADMIN".equals(user.getRole());
        if (path.startsWith("/admin") && !admin) {
            resp.sendRedirect(ctx + "/feedback");
            return;
        }
        if (path.equals("/feedback") && admin) {
            resp.sendRedirect(ctx + "/admin/dashboard");
            return;
        }
        // stop browser caching so Back button doesn't show pages after logout
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        chain.doFilter(rq, rs);
    }
}
