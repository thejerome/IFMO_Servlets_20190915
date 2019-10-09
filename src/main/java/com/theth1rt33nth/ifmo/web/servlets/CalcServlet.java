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
        urlPatterns = "/calc"
)
public class ServletCalc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        final Map<String, String[]> parameterMap= req.getParameterMap();
        String input = req.getParameter("input").replace(" ", "");
        while (!Pattern.matches("^[0-9*+-/()]+$", input)) {
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
                input = input.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }
        out.println(backend(input));
        out.flush();
        out.close();
    }

    private int backend (String target) {

        String eq = '(' + target + ')';
        ArrayDeque<String> queue = new ArrayDeque<>();
        Stack<Character> st = new Stack<>();
        int i = 0;
        while (i < eq.length()) {
            char ch = eq.charAt(i);
            if (Character.isDigit(ch)) {
                String num = "";
                while (Character.isDigit(eq.charAt(i))) {
                    num = num + eq.charAt(i);
                    i++;
                }
                queue.offer(num);
                i--;
            }
            else if (ch == '(') {
                st.push(ch);
            }
            else if (ch == ')') {
                while (st.peek() != '(')
                    queue.offer(st.pop().toString());

                st.pop();
            }
            else {
                while (st.peek() != '(' && !((ch == '*' || ch == '/') && (st
                        .peek() == '+' || st.peek() == '-')))
                    queue.offer(st.pop().toString());

                st.push(ch);
            }

            i++;
        }
        while (!st.empty()) {
            queue.offer(st.pop().toString());
        }
        Stack<Integer> refined = new Stack<>();
        while (!queue.isEmpty()) {
            String q = queue.peek();
            if (Pattern.matches("^[0-9]+$", q)) {
                refined.push(Integer.parseInt(q));
            }
            else {
                char op = queue.peek().charAt(0);
                int b = refined.pop();
                int a = refined.pop();
                int res = 0;
                if (op == '+')
                    res = a + b;
                else if (op == '-')
                    res = a - b;
                else if (op == '*')
                    res = a * b;
                else if (op == '/')
                    res = a / b;

                refined.push(res);
            }
            queue.poll();
        }
        return refined.pop();
    }
}