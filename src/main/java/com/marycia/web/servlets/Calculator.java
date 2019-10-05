package com.marycia.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;




@WebServlet(
        name = "Calculator",
        urlPatterns = "/calc"
)

public class Calculator extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        String equation = req.getParameter("equation");
        Map<String, String[]> prmts = req.getParameterMap();
        equation = equation.replaceAll("\\s+", "");

        while (consistOfLetters(equation)) {
            for (int i = 0; i < equation.length(); i++) {
                if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                    equation = equation.replace(String.valueOf(equation.charAt(i)), String.valueOf(prmts.get(String.valueOf(equation.charAt(i)))[0]));
                }
            }
        }

        MagicPolNotation n = new MagicPolNotation();
        List<String> expression = n.parse(equation);

        out.println(calc(expression));


    }

    private static Integer calc(List<String> postfix) {
        Deque<Integer> stack = new ArrayDeque<Integer>();
        for (String x : postfix) {
            if ("+".equals(x)) {
                stack.push(stack.pop() + stack.pop());
            } else if ("-".equals(x)) {
                Integer b = stack.pop();
                Integer a = stack.pop();
                stack.push(a - b);
            } else if ("*".equals(x)) {
                stack.push(stack.pop() * stack.pop());
            } else if ("/".equals(x)) {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a / b);
            } else stack.push(Integer.valueOf(x));
        }
        return stack.pop();
    }

    private boolean consistOfLetters(String tmp) {
        for (int i = 0; i < tmp.length(); i++) {
            if ('a' < tmp.charAt(i) && 'z' > tmp.charAt(i)) return true;
        }
        return false;
    }


private class MagicPolNotation {
    private  String operators = "+-*/";
    private  String delimiters = "() " + operators;

    private boolean isDelimiters (String tmp) {
        for (int i = 0; i < delimiters.length(); i++) {
            if (tmp.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }


    private int priority (String tmp) {
        if ("(".equals(tmp)) return 1;
        if ("+".equals(tmp) || "-".equals(tmp)) return 2;
        if ("*".equals(tmp) || "/".equals(tmp)) return 3;
        return 4; //Подумать, нужно ли 4, можно ли обойтись тремя, у нас нет функций
    }

    private List<String> parse (String infix) {
        List<String> postfix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (isDelimiters(curr)) {
                if ("(".equals(curr)) stack.push(curr);
                else if (")".equals(curr)) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                    if (!stack.isEmpty()) {
                        postfix.add(stack.pop());
                    }
                } else  {
                    while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                        postfix.add(stack.pop());
                    }
                    stack.push(curr);
                }
            } else {
                postfix.add(curr);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }
}
}
