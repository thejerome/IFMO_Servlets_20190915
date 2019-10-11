package com.mishep.webservlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "CalculatorServlet",
        urlPatterns = {"/calc"}
)
public class CalculatorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String equation = request.getParameter("equation");
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder stringBuilder = new StringBuilder();
        for (int symbol = 0; symbol < equation.length(); ++symbol){
            if(equation.charAt(symbol) >= 'a' && equation.charAt(symbol) <= 'z') {
                String param = paramMap.get(Character.toString(equation.charAt(symbol)))[0];
                while (param.charAt(0) >= 'a' && param.charAt(0) <= 'z') {
                    param = paramMap.get(param)[0];
                }
                stringBuilder.append(param);
            }
                else {
                    stringBuilder.append(equation.charAt(symbol));
                }
        }
        equation = stringBuilder.toString();
        PrintWriter out = response.getWriter();
        out.println("Your answer: " + Calculator.calc(equation));
    }
}
