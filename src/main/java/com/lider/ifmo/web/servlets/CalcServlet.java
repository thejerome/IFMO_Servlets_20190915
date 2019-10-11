package com.lider.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)
public class CalcServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        writer may be a bit more comfortable
//        ServletOutputStream out = resp.getOutputStream();
        PrintWriter out = resp.getWriter();

        //getting parameter
        String eq_str = req.getParameter("equation");
        Map<String, String[]> variables = req.getParameterMap();

        //eq_str = Equation.placeValues(eq_str, variables);
        System.out.println(eq_str);

        Equation equation = new Equation(variables);
        out.print(equation.Calculate(eq_str));
        System.out.println(eq_str);

        out.flush();
        out.close();
    }
}
