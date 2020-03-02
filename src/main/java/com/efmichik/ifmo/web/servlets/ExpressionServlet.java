package com.efmichik.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/calc/*"}, name = "expressionServlet")
public class ExpressionServlet extends HttpServlet {
    private final static String EQUATION_PARAMETER = "equation";
    private final static String SESSION_EXPRESSION = "expression";
    private final static String SESSION_VARIABLE = "variable_";
    private final static int LOWER_BOUND = -10000;
    private final static int HIGHER_BOUND = 10000;

    private int responseCode = 400;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] uriParts = req.getRequestURI().split("/");
        String lastUriPart = uriParts[uriParts.length - 1];
        String body = req.getReader().readLine();
        if (EQUATION_PARAMETER.equals(lastUriPart)) {
            setExpressionSession(req, body);
        } else {
            setVariableSession(req, lastUriPart, body);
        }
        resp.setStatus(responseCode);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] uriParts = req.getRequestURI().split("/");
        String lastUriPart = uriParts[uriParts.length - 1];
        if (lastUriPart.equals(EQUATION_PARAMETER)) {
            deleteSession(req, SESSION_EXPRESSION);
        } else {
            deleteSession(req, (SESSION_VARIABLE + lastUriPart));
        }
        resp.setStatus(responseCode);
    }

    private void setExpressionSession(HttpServletRequest req, String expression) {
        if (!validateExpression(expression)) {
            return;
        }
        HttpSession session = req.getSession(true);
        if (session.getAttribute(SESSION_EXPRESSION) != null) {
            responseCode = StatusCode.UPDATED.getCode();
        } else {
            responseCode = StatusCode.CREATED.getCode();
        }
        session.setAttribute(SESSION_EXPRESSION, expression);
    }

    private void setVariableSession(HttpServletRequest req, String variable, String value) {
        if (!validateValue(req, value)) {
            return;
        }
        HttpSession session = req.getSession(true);
        if (session.getAttribute(SESSION_VARIABLE + variable) != null) {
            responseCode = StatusCode.UPDATED.getCode();
        } else {
            responseCode = StatusCode.CREATED.getCode();
        }
        session.setAttribute((SESSION_VARIABLE + variable), value);
    }

    private void deleteSession(HttpServletRequest req, String attributeName) {
        HttpSession session = req.getSession(true);
        if (session.getAttribute(attributeName) != null) {
            responseCode = StatusCode.DELETED.getCode();
            session.removeAttribute(attributeName);
            return;
        }
        responseCode = StatusCode.INCORRECT.getCode();
    }

    private boolean validateExpression(String expression) {
        if (expression.matches("[^*/+-]+")) {
            responseCode = StatusCode.INCORRECT.getCode();
            return false;
        }
        return true;
    }

    private boolean validateValue(HttpServletRequest req, String value) {
        if (value.matches("[a-z]")) {
            String otherValue = getValue(req, value);
            if (!otherValue.isEmpty()) {
                return validateValue(req, otherValue);
            }
        }
        if (value.matches("[-0-9]+")) {
            int n = Integer.parseInt(value);
            boolean inInterval = n >= LOWER_BOUND && n <= HIGHER_BOUND;
            if (!inInterval) {
                responseCode = StatusCode.EXCEEDED.getCode();
            }
            return inInterval;
        }
        responseCode = StatusCode.INCORRECT.getCode();
        return false;
    }

    private String getValue(HttpServletRequest req, String variable) {
        HttpSession session = req.getSession(true);
        if (session.getAttribute(SESSION_VARIABLE + variable) != null) {
            return (String) session.getAttribute(SESSION_VARIABLE + variable);
        }
        return "";
    }
}
