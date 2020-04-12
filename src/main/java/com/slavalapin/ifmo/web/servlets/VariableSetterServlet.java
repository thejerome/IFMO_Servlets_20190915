package com.slavalapin.ifmo.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;



@WebServlet(
        name = "VariableSetterServlet",
        urlPatterns = {"/calc/*"}
)

public class VariableSetterServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String variableName = request.getRequestURI().substring(6);
        String variableValue = request.getReader().readLine();
        Pattern valueRegExp = Pattern.compile("^[a-z]{1}$|-?[0-9]+"); //single lower-case latin letter or a numeric value
        Matcher matcherValueRegExp = valueRegExp.matcher(variableValue);

        try {
            if (!matcherValueRegExp.matches()) {
                response.setStatus(400);
                throw new ServletException("Bad variable format.");
            } else {
                if (Character.isLetter(variableValue.charAt(0)) || Math.abs(Integer.parseInt(variableValue)) <= 10000) {
                    if (session.getAttribute(variableName) != null)
                        response.setStatus(200);
                    else
                        response.setStatus(201);
                    session.setAttribute(variableName, variableValue);
                } else {
                    response.setStatus(403);
                    throw new ServletException("Variable values should be in [-10000; 10000] range.");
                }
            }
        }
        catch (ServletException e) {
            PrintWriter body = response.getWriter();
            body.print(e.getMessage());
            body.flush();
            body.close();
        }

    }

    @Override
    protected void doDelete (HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String variableName = request.getRequestURI().substring(6);
        session.removeAttribute(variableName);
        response.setStatus(204);
    }
}