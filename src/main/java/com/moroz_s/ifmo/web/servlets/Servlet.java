package com.moroz_s.ifmo.web.servlets;

import java.util.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;


@WebServlet(
        name = "Servlet",
        urlPatterns = {"/calc/result"}
)
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String equation = ((String)session.getAttribute("equation")).replace(" ", "");
        StringBuilder reformed = reform(equation);
        boolean notEnoughData = false;
        ArrayDeque<String> cal = new ArrayDeque<>();

        try {
            calculation(session, reformed, cal);
            resp.setStatus(200);
        } catch (IllegalStateException e){
            resp.setStatus(409);
            resp.getWriter().print("...");
            notEnoughData = true;
        }

        if (!notEnoughData)
            resp.getWriter().print(cal.getFirst());
    }


    private StringBuilder reform(String equation){
        ArrayDeque<String> stack = new ArrayDeque<>();
        StringBuilder StringBuilder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(equation, "()+-*/", true);
        int length = tokenizer.countTokens();
        for (int i = 0; i < length; i++) {
            String token = tokenizer.nextToken();
            if (isLetter(token.charAt(0)) || Character.isDigit(token.charAt(0))) {
                StringBuilder.append(token).append('|');
            } else
            if (isOperator(token.charAt(0))) {
                if (stack.peek() != null) {
                    while (priority(stack.peek().charAt(0)) >= priority(token.charAt(0))) {
                        String c = stack.pop();
                        StringBuilder.append(c).append('|');
                        if (stack.peek() == null)
                            break;
                    }
                }
                stack.push(token);
            } else
            if (token.charAt(0) == '(') {
                stack.push(token);
            } else
            if (token.charAt(0) == ')') {
                while (!Objects.equals(stack.peek(), "(")) {
                    String op = stack.pop();
                    StringBuilder.append(op).append('|');
                }
                stack.pop();
            }
        }
        while (stack.peek() != null) {
            StringBuilder.append(stack.pop()).append('|');
        }
        StringBuilder.setLength(StringBuilder.length() - 1);
        System.out.println(StringBuilder.toString());
        return StringBuilder;
    }

    private void calculation(HttpSession session,StringBuilder reformed, ArrayDeque<String> calc){
        StringTokenizer tokenizer = new StringTokenizer(reformed.toString(), "|");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (isOperator(token.charAt(0))) {
                String right = calc.pop();
                String left = calc.pop();
                calc.push(calculate(token.charAt(0), Integer.parseInt(left), Integer.parseInt(right)));
            }
            else if (isLetter(token.charAt(0))) {
                String s = (String) session.getAttribute(token);
                if (s == null){
                    throw new IllegalStateException();
                }
                while (isLetter(s.charAt(0))) {
                    s = (String) session.getAttribute(s);
                    if (s == null){
                        throw new IllegalStateException();
                    }
                }
                calc.push(s);
            } else {
                calc.push(token);
            }
        }
    }

    private byte priority(char symb) {
        if (symb == '+' || symb == '-')
            return 3;
         if (symb == '/' || symb == '*')
            return 8;
         
         return 0;
    }

    private boolean isOperator(char symb){
        return symb == '+' | symb == '-' | symb == '*' | symb == '/';
    }

    private boolean isLetter (char symb){
        return symb >='a' &&  symb <='z';
    }

    private String calculate(char operation, int leftArgument, int rightArgument) {
        switch (operation) {
            case '+':
                return String.valueOf(leftArgument + rightArgument);
            case '-':
                return String.valueOf(leftArgument - rightArgument);
            case '/':
                return String.valueOf(leftArgument / rightArgument);
            case '*':
                return String.valueOf(leftArgument * rightArgument);
            default:
                return "impossible";
        }
    }

}