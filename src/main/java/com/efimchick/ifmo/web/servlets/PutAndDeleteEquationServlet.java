package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "PutAndDeleteEquationServlet",
        urlPatterns = {"/calc/equation"}
)
public class PutAndDeleteEquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String s = req.getReader().readLine();
        if (badFormatted(s)) {
            resp.setStatus(400);
            resp.getWriter().println("bad format");
        } else {
            if (session.getAttribute("equation") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            session.setAttribute("equation", s);
        }
    }

    private boolean badFormatted(String s) {
        for (int i = 1; i<s.length(); ++i){
            char cur = s.charAt(i);
            char prev = s.charAt(i-1);
            if (Character.isLetter(cur) && Character.isLetter(prev))
                return true;
        }
        return false;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("equation", null);
        resp.setStatus(204);
    }
}