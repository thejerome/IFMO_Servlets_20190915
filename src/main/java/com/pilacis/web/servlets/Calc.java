package com.pilacis.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(
        name = "Calc",
        urlPatterns = {"/calc"}
)
public class Calc extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        Parser parser = new Parser(req.getParameterMap(), req.getParameter("equation"));
        parser.Parse();

        out.println(parser.evaluate());
        out.flush();
        out.close();
    }

}
