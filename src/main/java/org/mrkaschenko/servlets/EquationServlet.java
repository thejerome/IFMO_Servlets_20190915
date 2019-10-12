package org.mrkaschenko.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

// @WebServlet(
//     name = "EquationServlet",
//     urlPatterns = {"/calc/equation"}
// )
public class EquationServlet extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

            PrintWriter out = response.getWriter();
            BufferedReader in = request.getReader();
            String value = in.readLine();

            String key = "equation";

            Cookie[] cookies = request.getCookies();
            Cookie cookie = null;
            boolean valueExists = false;

            System.out.print("\nEquation Servlet Middle\n");
            //System.out.print("\nCookies length: \n" + cookies.length);

            if(cookies!=null) {
                for(int i=0; i<cookies.length; i++) {
                    if(key.equals(cookies[i].getName())) {
                        valueExists = true;
                        cookies[i].setValue(value);
                        response.setStatus(200);
                    }
                }
            }
            if(!valueExists) {
                cookie = new Cookie(key,value);
                response.addCookie(cookie);
                response.setStatus(201);
            }

            if(cookies!=null) {
                out.print("---------------------\n");
                for(int i=0; i<cookies.length; i++) {
                    out.print(cookies[i].getName() + ": " + cookies[i].getValue() + "\n");
                }
                out.print("---------------------\n");
            }
            out.print(value);

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String key = "equation";

        Cookie cookie = new Cookie(key,"");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(204);
    }
}
