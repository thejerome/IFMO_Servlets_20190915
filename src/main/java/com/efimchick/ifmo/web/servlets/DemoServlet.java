package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
    name = "DemoServlet",
    urlPatterns = {"/demo"}
)
public class DemoServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        out.write("Hello, world".getBytes());
        out.flush();
        out.close();
    }

}
