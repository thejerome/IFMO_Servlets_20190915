package com.efmichik.ifmo.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/calc/result"})
public class CalcFilter implements Filter {
    private final static String SESSION_EXPRESSION = "expression";
    private final static String SESSION_VARIABLE = "variable_";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String expression = getExpression((HttpServletRequest) request);
        if (expression.isEmpty()) {
            ((HttpServletResponse) response).sendError(StatusCode.UNCALCULATED.getCode(), "Uncalculated expression");
            return;
        }
        if (!checkVariables((HttpServletRequest) request, expression)) {
            ((HttpServletResponse) response).sendError(StatusCode.UNCALCULATED.getCode(), "Unknown variable");
            return;
        }
        chain.doFilter(request, response);
    }

    private String getExpression(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute(SESSION_EXPRESSION) != null) {
            return (String) session.getAttribute(SESSION_EXPRESSION);
        }
        return "";
    }

    private boolean checkVariables(HttpServletRequest req, String expression) {
        HttpSession session = req.getSession();
        String[] vars = expression.split("[()\\s\\d*\\-+/]");
        for (String var : vars) {
            if (!var.isEmpty() && session.getAttribute(SESSION_VARIABLE + var) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Unnecessary method
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Hate codacy
    }

    /**
     * Unnecessary method
     */
    @Override
    public void destroy() {
        //Dumb codacy
    }
}
