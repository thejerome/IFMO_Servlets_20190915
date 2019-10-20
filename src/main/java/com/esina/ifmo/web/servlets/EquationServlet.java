package com.esina.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "EquationServlet",
        urlPatterns = {"/calc/equation"}
)

public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        String equation = req.getReader().readLine();

        if (session.getAttribute("equation") == null)
            resp.setStatus(201);
        else
            resp.setStatus(200);

        session.setAttribute("equation", equation);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("fuuucckkk eq");

        req.getSession(false).removeAttribute("equation");
        resp.setStatus(204);
    }

}