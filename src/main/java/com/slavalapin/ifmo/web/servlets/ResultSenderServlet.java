package com.slavalapin.ifmo.web.servlets;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;




@WebServlet(
        name = "ResultSenderServlet",
        urlPatterns = "/calc/result"
)

public class ResultSenderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        PrintWriter responseBody = response.getWriter();

        try {
            if (session.getAttribute("equation") == null)
                throw new ServletException("No equation.");

            String expression = session.getAttribute("equation").toString();
            try {
                responseBody.print(CalculatorInternal.doMagic(expression, sessionMap(session)));
                response.setStatus(200);
            }
            catch (MissingResourceException e) {
                throw new ServletException("Missing variable: " + e.getKey());
            }

        }
        catch (ServletException e) {
            response.setStatus(409);
            responseBody.print(e.getMessage());
        }
        finally {
            responseBody.flush();
            responseBody.close();
        }
    }

    private Map<String, String> sessionMap(HttpSession session) {
        Map<String, String> variablesMap = new HashMap<>();
        Enumeration<String> sessionAttributes = session.getAttributeNames();
        while (sessionAttributes.hasMoreElements()) {
            String key = sessionAttributes.nextElement();
            String value = session.getAttribute(key).toString();
            variablesMap.put(key, value);
        }
        variablesMap.remove("equation");
        return variablesMap;
    }
}