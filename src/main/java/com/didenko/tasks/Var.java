package com.didenko.tasks;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "Var",
        urlPatterns = {"/calc/*"}
)
public class Var extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String var = req.getRequestURI().substring(6);
        if (session.getAttribute(var) != null) {
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
        }
        String varVal = req.getReader().readLine();
        if (!(varVal.charAt(0) >= 'a' && varVal.charAt(0) <= 'z')) {
            int i = Integer.parseInt(varVal);
            if (i < -10000 || i > 10000) {
                resp.setStatus(403);
                resp.getWriter().write("...");
            } else
                session.setAttribute(var, varVal);
        } else
            session.setAttribute(var, varVal);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(204);
        req.getSession().removeAttribute(req.getRequestURI().substring(6));
    }
}
