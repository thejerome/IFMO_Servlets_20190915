package com.Piskov.web.servlets;

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
        name = "Servlet",
        urlPatterns = {"/calc"}
)
public class Servlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws  IOException {

        PrintWriter out = resp.getWriter();
        String equation = URLMapper.Map(req.getParameterMap(),req.getParameter("equation"));
        out.print (Calculator.Calculate(equation));
        out.flush();
        out.close();
    }

}
