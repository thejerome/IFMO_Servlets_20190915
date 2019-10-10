package com.theth1rt33nth.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)
public class CalcServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        final Map<String, String[]> parameterMap= req.getParameterMap();
        String equation = req.getParameter("equation").replace(" ", "");
        while (!Pattern.matches("^[0-9*+-/()]+$", equation)) {
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }
        out.println(backend(equation));

        out.flush();
        out.close();
    }
    private int backend (String target) {
        String eq = '(' + target + ')';
        ArrayDeque<String> q = new ArrayDeque<>();
        Stack<Character> st = new Stack<>();
        int i = 0;
        while (i < eq.length()) {
            char c = eq.charAt(i);
            if (Character.isDigit(c)) {
                String num = "";
                while (Character.isDigit(eq.charAt(i))) {
                    num = num + eq.charAt(i);
                    i++;
                }
                q.offer(num);
                i--;
            }
            else if (c == '(') {
                st.push(c);
            }
            else if (c == ')') {
                while (st.peek() != '(')
                    q.offer(st.pop().toString());
                st.pop();
            }
            else {
                while (st.peek() != '(' && !((c == '*' || c == '/') && (st.peek() == '+' || st.peek() == '-')))
                    q.offer(st.pop().toString());
                st.push(c);
            }
            i++;
        }
        Stack<Integer> polStack = new Stack<>();
        while (!q.isEmpty()) {
            String qq = q.peek();
            if (Pattern.matches("^[0-9]+$", qq)) {
                polStack.push(Integer.parseInt(qq));
            }
            else {
                char op = q.peek().charAt(0);
                int b = polStack.pop();
                int a = polStack.pop();
                int res = 0;
                switch (op) {
                    case  ('+'):
                        res = a + b;
                        break;
                    case ('-'):
                        res = a - b;
                        break;
                    case ('*'):
                        res = a * b;
                        break;
                    case ('/'):
                        res = a / b;
                        break;
                }
                polStack.push(res);
            }
            q.poll();
        }
        return polStack.pop();
    }
    }
}