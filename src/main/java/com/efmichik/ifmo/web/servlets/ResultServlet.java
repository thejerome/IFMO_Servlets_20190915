package com.efmichik.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

@WebServlet(urlPatterns = {"/calc/result"})
public class ResultServlet extends HttpServlet {
    private final static String SESSION_VARIABLE = "variable_";
    private final static String SESSION_EXPRESSION = "expression";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String expression = getExpression(req);
        int result;
        result = evaluate(expression, req);
        int responseCode = StatusCode.CALCULATED.getCode();
        resp.setStatus(responseCode);
        resp.getWriter().print(result);
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    private int evaluate(String expression, HttpServletRequest req) {
        char[] tokens = expression.toCharArray();
        Deque<Integer> values = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();

        for (char token : tokens) {
            if (token == ' ') {
                continue;
            }

            if (token >= '0' && token <= '9') {
                values.offerFirst(Character.getNumericValue(token));
            } else if (token == '(') {
                ops.offerFirst(token);
            } else if (token == ')') {
                while (ops.getFirst() != '(') {
                    values.offerFirst(applyOperation(ops.removeFirst(), values.removeFirst(), values.removeFirst()));
                }
                ops.pollFirst();
            } else if (token == '+' || token == '-' || token == '*' || token == '/') {
                while (!ops.isEmpty() && hasPrecedence(token, ops.peekFirst())) {
                    values.offerFirst(applyOperation(ops.pollFirst(), values.removeFirst(), values.removeFirst()));
                }
                ops.offerFirst(token);
            } else {
                values.offerFirst(getVariableValue(req, Character.toString(token)));
            }
        }

        while (!ops.isEmpty()) {
            values.offerFirst(applyOperation(ops.pollFirst(), values.removeFirst(), values.removeFirst()));
        }

        return values.removeFirst();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private int applyOperation(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
            default:
                return 0;
        }
    }

    private int getVariableValue(HttpServletRequest req, String variable) {
        HttpSession session = req.getSession();
        String value = (String) session.getAttribute(SESSION_VARIABLE + variable);
        if (value.matches("[a-z]")) {
            return getVariableValue(req, value);
        }
        return Integer.parseInt(value);
    }

    private String getExpression(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session.getAttribute(SESSION_EXPRESSION);
    }
}
