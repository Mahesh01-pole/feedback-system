package com.feedback.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** One-time message that survives a redirect (Post/Redirect/Get). */
public final class Flash {
    private Flash() {}

    public static void set(HttpServletRequest req, String type, String message) {
        HttpSession s = req.getSession();
        s.setAttribute("flashType", type);
        s.setAttribute("flashMsg", message);
    }

    public static void consume(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return;
        Object msg = s.getAttribute("flashMsg");
        if (msg != null) {
            req.setAttribute("msgType", s.getAttribute("flashType"));
            req.setAttribute("msg", msg);
            s.removeAttribute("flashMsg");
            s.removeAttribute("flashType");
        }
    }
}
