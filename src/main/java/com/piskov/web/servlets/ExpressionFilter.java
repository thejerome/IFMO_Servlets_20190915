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
        boolean isError = false;

        if (reqURL.equals("equation")) {
            String equation = req.getReader().readLine();
            req.getReader().reset();
            if (!isExpression(equation)) {
                res.setStatus(400);
                res.getWriter().write("Bad FORMAT");
                isError = !isExpression(equation);
            }
            req.getReader().reset();
        } else if (!reqURL.equals("result")) {
            try {
                int value = Integer.parseInt(req.getReader().readLine());
                req.getReader().reset();
                if (!isInRange(value)) {
                    res.setStatus(403);
                    isError = !isInRange(value);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        req.getReader().reset();


        if (!isError) {
            chain.doFilter(request, response);
        }
    }

    private static boolean isExpression(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('*' <= expression.charAt(i) && '/' >= expression.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isInRange(int value){
        return (value <= 10000 && value >= -10000);
    }

    @Override
    public void destroy() {
    }
}
