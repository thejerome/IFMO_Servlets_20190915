package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
    name = "DemoServlet6",
    urlPatterns = {"/demo6"}
)
public class DemoServlet6 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.print("<html>\n" +
            "  <head>\n" +
            "    <title>demo6</title>\n" +
            "  </head>\n" +
            "  <body>\n");

        final String name = req.getParameter("name");
        out.print("<h3>Hello, " + name + "</h3>");

        out.print("  </body>\n" +
            "</html>");
        out.flush();
        out.close();
    }

}
