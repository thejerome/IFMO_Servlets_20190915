package com.moroz_s.ifmo.web.servlets;

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
        String variable = req.getRequestURI().substring(6);
        HttpSession session = req.getSession();

        if (session.getAttribute(variable) != null) {
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
        }
        String varVal = req.getReader().readLine();
        if (!isLetter(varVal.charAt(0))) {
            int i = Integer.parseInt(varVal);
            if (i*i > 100000000) {
                resp.setStatus(403);
                resp.getWriter().print("...");
            } else
                session.setAttribute(variable, varVal);
        } else
            session.setAttribute(variable, varVal);
    }

    private boolean isLetter (char symb){
        return symb >='a' &&  symb <='z';
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(204);
        String variable = req.getRequestURI().substring(6);
        req.getSession().setAttribute(variable, null);
    }
}