package com.marycia.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "VarOfEquationServlet",
        urlPatterns = {"/calc/*"}
)

public class VarOfEquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String key = req.getRequestURI().substring(6);
        String value = req.getReader().readLine();

        if ((value.charAt(0) >= 'a' && value.charAt(0) <= 'z') || (Integer.valueOf(value) > -10000 && Integer.valueOf(value) < 10000) ) {
            if (session.getAttribute(key) != null) {
                resp.setStatus(200);
            } else {
                resp.setStatus(201);
            }
            session.setAttribute(key,value);
        } else {
            resp.setStatus(403);
        }
    }

    @Override
    protected void doDelete (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        String key = req.getRequestURI().substring(6);
        session.removeAttribute(key);
        resp.setStatus(204);
    }
}

