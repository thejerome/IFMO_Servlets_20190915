package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
    name = "DemoServlet4",
    urlPatterns = {"/demo4"}
)
public class DemoServlet4 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.print("<html>\n" +
            "  <head>\n" +
            "    <title>demo4</title>\n" +
            "  </head>\n" +
            "  <body>\n");

        //do not create session if it does not exist
        HttpSession session = req.getSession(false);
        if(session == null){
            //create or get session
            //the same as req.getSession(true)
            session = req.getSession();
            session.setMaxInactiveInterval(360);

            session.getId();
            session.getCreationTime();
            session.getLastAccessedTime();

            session.setAttribute("name", "stranger");
        }

        // get object from session
        final String name = (String) session.getAttribute("name");
        out.print("<h3>Hello, "+ name + "</h3>");

        session.setAttribute("name", name.isEmpty() ? "stranger": name.substring(1));
        
        out.print("  </body>\n" +
            "</html>");
        out.flush();
        out.close();
    }

}
