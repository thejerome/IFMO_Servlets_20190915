package com.sam.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(
        name = "Calculator",
        urlPatterns = {"/calc"}
)

public class Calculator extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        //getting parameter
        String expression = req.getParameter("equation");
        Map<String, String[]> prmts = req.getParameterMap();
        expression = VarInMap.inMap(prmts, expression);
        out.print(Parser.answer_mthd(Parser.parse(expression)));
        out.flush();
        out.close();
    }

}