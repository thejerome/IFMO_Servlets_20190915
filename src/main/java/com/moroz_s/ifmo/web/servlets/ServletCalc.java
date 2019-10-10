package com.moroz_s.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "ServletCalc",
        urlPatterns = "/calc"
)
public class ServletCalc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        final Map<String, String[]> parameterMap= req.getParameterMap();

        String equation = req.getParameter("equation").replace(" ", "");

        while (!Pattern.matches("^[0-9*+-/()]+$", equation))
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) 
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            


        out.println(evaluation(equation));

        out.flush();
        out.close();
    }

    private int evaluation (String toCalc) {
        String eq = '(' + toCalc + ')';

        Stack<Character> lineStack = new Stack<>();
        ArrayDeque<String> line = new ArrayDeque<>();


        int i = 0;
        while (i < eq.length()) {
            char ch = eq.charAt(i);

            if (Character.isDigit(ch)) {
                String num = "";


                while (Character.isDigit(eq.charAt(i))) {
                    num = num + eq.charAt(i);
                    i++;
                }

                line.offer(num);
                i--;
            }


            else if (ch == '(') {
                lineStack.push(ch);
            }
            else if (ch == ')') {
                while (lineStack.peek() != '(')
                    line.offer(lineStack.pop().toString());

                lineStack.pop();
            }

            else {
                while (lineStack.peek() != '(' && !((ch == '*' || ch == '/') && (lineStack
                        .peek() == '+' || lineStack.peek() == '-')))
                    line.offer(lineStack.pop().toString());

                lineStack.push(ch);
            }

            i++;
        }

        while (!lineStack.empty()) {
            line.offer(lineStack.pop().toString());
        }

        Stack<Integer> polStack = new Stack<>();

        while (!line.isEmpty()) {
            String q = line.peek();

            if (Pattern.matches("^[0-9]+$", q)) {
                polStack.push(Integer.parseInt(q));
            }
            else {
                char operation = line.peek().charAt(0);
                int b = polStack.pop();
                int a = polStack.pop();

                int res = 0;

                if (operation == '+')
                    res = a + b;
                else if (operation == '-')
                    res = a - b;
                else if (operation == '*')
                    res = a * b;
                else if (operation == '/')
                    res = a / b;

                polStack.push(res);
            }

            line.poll();
        }

        return polStack.pop();
    }
}