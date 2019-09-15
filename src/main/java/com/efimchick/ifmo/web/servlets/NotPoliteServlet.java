package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
    name = "NotPolite",
    urlPatterns = {"/not_polite"}
)
public class NotPoliteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
//        writer may be a bit more comfortale
//        ServletOutputStream out = resp.getOutputStream();
        PrintWriter out = resp.getWriter();
        out.print("You are not polite, go away");
        out.flush();
        out.close();
    }

}
