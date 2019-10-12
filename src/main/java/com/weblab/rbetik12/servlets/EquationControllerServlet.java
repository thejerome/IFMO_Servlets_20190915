package com.weblab.rbetik12.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "EquationControllerServlet",
        urlPatterns = {"/calc/equation"}
)
public class EquationControllerServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("equation") == null) {
            resp.setStatus(201);
        } else {
            resp.setStatus(200);
        }
        String equation = req.getReader().readLine();
        httpSession.setAttribute("equation", equation);
        resp.getWriter().write(equation);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
