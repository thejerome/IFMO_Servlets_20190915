package com.burtseva.task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "com.burtseva.task.Var", urlPatterns = {"/calc/*"})

public class Var extends HttpServlet {
    private boolean correctValue(String val) {
        return (val.charAt(0) >= 'a' && val.charAt(0) <= 'z') || (Integer.parseInt(val) >= -10000 && Integer.parseInt(val) <= 10000);
    }

    @Override
    protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession();
        PrintWriter writer = httpServletResponse.getWriter();
        String val = httpServletRequest.getReader().readLine();
        String URI = httpServletRequest.getRequestURI();
        String var = String.valueOf(URI.charAt(URI.length() - 1));
        if (!correctValue(val)) {
            writer.println("value isn't correct or out of range");
            httpServletResponse.setStatus(403);
        }
        else {
            if (session.getAttribute(var) == null) {
                httpServletResponse.setStatus(201);
                session.setAttribute(var, val);
            }
            else {
                httpServletResponse.setStatus(200);
                session.setAttribute(var, val);
            }
        }
        writer.close();
    }

    @Override
    protected void doDelete (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String URI = httpServletRequest.getRequestURI();
        String var = String.valueOf(URI.charAt(URI.length() - 1));
        httpServletRequest.getSession().removeAttribute(var);
        httpServletResponse.setStatus(204);
    }
}
