package com.webprog.task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.StringTokenizer;

@WebServlet(
        name = "GetServlet",
        urlPatterns = {"/calc/result"}
)
public class GetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        PrintWriter writer = resp.getWriter();
        if (session == null) {
            resp.setStatus(409);
            writer.write("BAD FORMAT");
        } else {
            String equation = (String) session.getAttribute("equation");
            if (equation == null) {
                resp.setStatus(409);
                writer.write("BAD FORMAT");
            } else {
                equation = equation.replaceAll("\\s","");
                String RPN = getReversePolishNotation(equation);
                String result = "";
                try {
                    result = getResult(req, RPN);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(409);
                    writer.write("BAD FORMAT");
                }
                if (resp.getStatus() != 409) {
                    resp.setStatus(200);
                    writer.write(result);
                } else {
                    writer.write("BAD FORMAT!");
                }
                writer.flush();
                writer.close();
            }
        }
    }

    private String getResult(HttpServletRequest req, String RPN) {
        StringTokenizer st = new StringTokenizer(RPN.toString(), ".");
        ArrayDeque<String> calc = new ArrayDeque<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumber(token)) {
                calc.push(token);
                continue;
            }
            if (isVar(token)) {
                String valueOfVar;
                valueOfVar = getValueOfVar(req, token);
                calc.push(valueOfVar);
                continue;
            }
            if (isOperator(token)) {
                if (calc.size() < 2) throw new AssertionError();
                String rhs = calc.pop();
                String lhs = calc.pop();
                calc.push(calcSimpleEquation(Integer.parseInt(lhs), Integer.parseInt(rhs), token));
            }
        }
        return calc.getFirst();
    }

    private String getReversePolishNotation(String equation) {
        StringBuilder rpn = new StringBuilder();
        ArrayDeque<String> opStack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(equation, "+-*/()", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumberOrVar(token)) {
                rpn.append(token);
                rpn.append('.');
            }
            if (token.equals("(")) {
                opStack.push(token);
            }
            if (token.equals(")")) {
                while (!Objects.equals(opStack.peek(), "(")) {
                    String op = opStack.pop();
                    rpn.append(op);
                    rpn.append('.');
                }
                opStack.pop();
            }
            if (isOperator(token)) {
                if (opStack.peek() != null) {
                    while (priorityOfOperator(opStack.peek()) >= priorityOfOperator(token)) {
                        String op = opStack.pop();
                        rpn.append(op);
                        rpn.append('.');
                        if (opStack.peek() == null)
                            break;
                    }
                }
                opStack.push(token);
            }
        }
        while (opStack.peek() != null) {
            String op = opStack.pop();
            rpn.append(op);
            rpn.append('.');
        }
        rpn.setLength(rpn.length() - 1);
        return rpn.toString();
    }

    private String calcSimpleEquation(int lhs, int rhs, String op) {
        char c = op.charAt(0);
        switch (c) {
            case '+':
                return String.valueOf(lhs + rhs);
            case '-':
                return String.valueOf(lhs - rhs);
            case '*':
                return String.valueOf(lhs * rhs);
            case '/':
                return String.valueOf(lhs / rhs);
        }
        return "";
    }

    private boolean isNumberOrVar(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z'))
                    return false;
            }
        }
        return true;
    }

    private boolean isVar(String str) {
        if (str.length() != 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z'))
                return false;
        }
        return true;
    }

    private boolean isNumber(String str) {
        if (str.charAt(0) == '-' && str.length() == 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-')
                continue;
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String getValueOfVar(HttpServletRequest req, String a) {
        HttpSession session = req.getSession(false);
        String s = (String) session.getAttribute(a);
        if (s == null) {
            throw new IllegalArgumentException();
        }
        while (!isNumber(s)) {
            s = (String) session.getAttribute(s);
            if (s == null) {
                throw new IllegalArgumentException();
            }
        }
        return s;
    }

    private boolean isOperator(String op) {
        char c = op.charAt(0);
        return c == '+' || c == '-' || c == '/' || c == '*';
    }

    private int priorityOfOperator(String op) {
        char c = op.charAt(0);
        if (c == '*' || c == '/')
            return 2;
        else if (c == '+' || c == '-')
            return 1;
        else
            return 0;
    }

}