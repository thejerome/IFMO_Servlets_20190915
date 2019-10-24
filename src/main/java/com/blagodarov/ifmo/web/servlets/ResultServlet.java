package com.blagodarov.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.regex.Pattern;


@WebServlet(
        name = "ResultServlet",
        urlPatterns = {"/calc/result"}
)

public class ResultServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();

        try {
            HttpSession session = req.getSession();

            if (session == null)
                throw new ServletException("Empty session!");
            String equation = session.getAttribute("equation").toString();
            if (equation == null)
                throw new ServletException("Equation does not exist!");
            String varList = session.getAttribute("varList").toString();
            if (varList == null)
                throw new ServletException("There are no variables at all!");
            
            //insert values in expression
            StringBuilder equation2 = new StringBuilder();
            for (int i = 0; i < equation.length(); i++){
                char var = equation.charAt(i);
                if (isLetter(equation.substring(i, i + 1))) {
                    String value = session.getAttribute(equation.substring(i, i + 1)).toString();
                    while (isLetter(value)) {
                        value = session.getAttribute(String.valueOf(value)).toString();
                    }
                    equation2.append(value);
                }
                else
                    equation2.append(var);
            }
            equation = equation2.toString();
            if (!Pattern.matches("^[0-9*+-/()]+$", equation)){
                throw new ServletException("Value is missing!");
            }
            writer.print(CalcUtil.eval(equation));
            resp.setStatus(200);

        } catch (Exception e) {
            resp.setStatus(409);
            writer.print(e.getMessage());
        } finally {
            writer.flush();
            writer.close();
        }
    }
    private boolean isLetter(String s){
        return s.charAt(0)>='a' && s.charAt(0)<='z';
    }
}
