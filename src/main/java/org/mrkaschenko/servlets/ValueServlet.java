package org.mrkaschenko.servlets;

import java.io.IOException;
//import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ValueServlet extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

            //PrintWriter out = response.getWriter();
            String value = request.getAttribute("value").toString();
            request.removeAttribute("value");

            String uri = request.getRequestURI();
            String key = uri.substring(uri.length() - 1);

            HttpSession session = request.getSession(false);
            Enumeration e = session.getAttributeNames();
            boolean valueExists = false;

            if(e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String name = (String) e.nextElement();
                    if(key.equals(name)) {
                        valueExists = true;
                        session.setAttribute(key, value);
                        response.setStatus(200);
                    }
                }
            }
            if(!valueExists) {
                session.setAttribute(key,value);
                response.setStatus(201);
            }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String uri = request.getRequestURI();
        String key = uri.substring(uri.length() - 1);

        HttpSession session = request.getSession(false);
        session.removeAttribute(key);
        response.setStatus(204);
    }
}
