package com.efimchick.ifmo.web.servlets;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;

@WebServlet(
        name = "Servlet1",
        urlPatterns = {"/calc"}
)
public class Servlet1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        while (hasVars(equation)){
            StringBuilder withValues = new StringBuilder();
            Map<String, String[]> params = req.getParameterMap();
            for (int i = 0; i < equation.length(); i++) {
                char elem = equation.charAt(i);
                if (Character.isLetter(elem)) {
                    String result = params.get(Character.toString(elem))[0];
                    withValues.append(result);
                }
                else {
                    withValues.append(elem);
                }
            }
            equation = withValues.toString();
        }
        ArrayDeque<String> rpn = shuntingYard(equation);
        out.println(calculate(rpn));
        out.flush();
        out.close();
    }

    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm#The_algorithm_in_detail
    private ArrayDeque<String> shuntingYard(String infix) {
        ArrayDeque<String> postfix = new ArrayDeque<>();
        ArrayDeque<String> operStack = new ArrayDeque<>();
        StringBuilder temp = new StringBuilder();
        int lastOper = 0;
        boolean negative = false;
        char token;
        String top;

        for (int i = 0; i < infix.length(); i++) {
            token = infix.charAt(i);
            if (Character.isDigit(token)) {
                temp.append(token);
            }
            if (!Character.isDigit(token) || i == infix.length() - 1) {
                if (negative) {
                    postfix.offerLast("-" + temp.toString());
                    negative = false;
                }
                else {
                    postfix.offerLast(temp.toString());
                }
                temp.delete(0, temp.length());
            }

            if (isOper(token)) {
                if ((lastOper == i-1 || operStack.peekLast() == "(") && token == '-') {
                    negative = true;
                }
                else {
                    while (operStack.peekLast() != null && precedence(token) <= precedence(operStack.peekLast().charAt(0)) &&
                            !operStack.peekLast().equals("(")) {
                        postfix.offerLast(operStack.pollLast());
                    }
                    operStack.offerLast(Character.toString(token));
                    lastOper = i;
                }
            }

            if (token == '(') {
                operStack.offerLast(Character.toString(token));
            }

            if (token == ')') {
                top = operStack.peekLast();
                while (!top.equals("(")) {
                    postfix.offerLast(operStack.pollLast());
                    top = operStack.peekLast();
                }
                operStack.pollLast();
            }
        }

        while (!operStack.isEmpty()) {
            postfix.offerLast(operStack.pollLast());
        }
        return postfix;
    }

    private int precedence(char oper) {
        if (oper == '/' || oper == '*') {
            return 2;
        }
        else {
            return 1;
        }
    }

    private boolean isOper(char input) {
        return input == '-' || input == '+' || input == '*' || input == '/';
    }

    private int calculate(ArrayDeque<String> input) {
        String elem;
        Integer operand1;
        Integer operand2;
        Integer value;
        ArrayDeque<Integer> result = new ArrayDeque<>();
        for (String s : input) {
            elem = s;
            if (elem.length() == 1 && isOper(elem.charAt(0))) {
                operand2 = result.pollLast();
                operand1 = result.pollLast();
                if (elem.equals("+")) {
                    value = operand1 + operand2;
                    result.offerLast(value);
                }
                if (elem.equals("-")) {
                    value = operand1 - operand2;
                    result.offerLast(value);
                }
                if (elem.equals("*")) {
                    value = operand1 * operand2;
                    result.offerLast(value);
                }
                if (elem.equals("/")) {
                    value = operand1 / operand2;
                    result.offerLast(value);
                }
            }
            else {
                if (!elem.equals("")) {
                    result.offerLast(Integer.valueOf(elem));
                }
            }
        }
        return result.pollLast();
    }

    private boolean hasVars(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}