package com.moroz_s.ifmo.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(
        name = "Equation",
        urlPatterns = {"/calc/equation"}
)
public class Equation extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String eq = req.getReader().readLine();

        if (session.getAttribute("equation") == null) {
            resp.setStatus(201);
        } else {
            resp.setStatus(200);
        }
        if (badFormed(eq)) {
            resp.setStatus(400);
            resp.getWriter().print("...");
        } else
            session.setAttribute("equation", eq);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute("equation");
        resp.setStatus(204);
    }


    protected boolean badFormed(String expression) {
        byte steps = 0;
        for (char op : expression.toCharArray())
            if (op == '+' || op == '-' || op == '/' || op == '*') {
                steps++;
            }
        return steps == 0;
    }

}