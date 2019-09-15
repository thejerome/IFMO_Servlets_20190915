package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
    name = "DemoServlet2",
    urlPatterns = {"/demo2"}
)
public class DemoServlet2 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.print("<html>\n" +
            "  <head>\n" +
            "    <title>demo2</title>\n" +
            "  </head>\n" +
            "  <body>\n");

        //All parameters
        final Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            out.println("<h3>" +
                parameterEntry.getKey() +
                ": " +
                Arrays.deepToString(parameterEntry.getValue()) +
                "</h3>");
        }


        //getting parameter
        final String name = req.getParameter("name");
        out.print("Hello, " + name);

        out.print("  </body>\n" +
            "</html>");
        out.flush();
        out.close();
    }

}
