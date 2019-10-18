package com.didenko.tasks;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        if (session.getAttribute("equation") != null) {
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
        }
        if (!good(eq)) {
            resp.setStatus(400);
            resp.getWriter().write("...");
        } else
            session.setAttribute("equation", eq);
    }

    private boolean good(String eq) {
        int cnt = 0;
        for (char c : eq.toCharArray())
            if (c == '+' || c == '-' || c == '/' || c == '*') {
                cnt++;
            }
        return cnt != 0;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute("equation");
        resp.setStatus(204);
    }
}
