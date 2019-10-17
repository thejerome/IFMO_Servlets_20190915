package com.piskov.web.servlets;


import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(
        urlPatterns = "/calc/*"
)
public class ExpressionFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String reqURL = req.getRequestURI().substring(6);
        boolean error = false;

        if (reqURL.equals("equation")) {
            String equation = req.getReader().readLine();
            if (!isExpression(equation)) {
                res.setStatus(400);
                res.getWriter().write("Bad formatted");
                error = true;
            }
            req.getReader().reset();
        } else if (Pattern.matches("[a-z]", reqURL) && !"result".equals(reqURL)) {
            try {
                String reqBody = req.getReader().readLine();
                int value = Integer.parseInt(reqBody);
                if (value > 10000 || value < -10000) {
                    res.setStatus(403);
                    error = true;
                }
            } catch (NumberFormatException ignored) {
            } finally {
                req.getReader().reset();
            }
        }


        if (!error) {
            chain.doFilter(request, response);
        }
    }

    private static boolean isExpression(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('*' <= expression.charAt(i) && '/' >= expression.charAt(i)) return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
