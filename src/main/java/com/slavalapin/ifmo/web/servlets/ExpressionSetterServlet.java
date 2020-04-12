package com.slavalapin.ifmo.web.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;



@WebServlet(
        name = "ExpressionSetterServlet",
        urlPatterns = {"/calc/equation"}
)

public class ExpressionSetterServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String expression = request.getReader().readLine();
        HttpSession session = request.getSession();

        if (session.getAttribute("equation") == null)
            response.setStatus(201);
        else
            response.setStatus(200);

        session.setAttribute("equation", expression);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        request.getSession(false).removeAttribute("equation");
        response.setStatus(204);
    }

}
