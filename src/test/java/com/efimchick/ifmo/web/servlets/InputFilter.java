package com.efimchick.ifmo.web.servlets;
import javax.servlet.annotation.WebFilter;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebFilter(
        filterName = "InputFilter",
        urlPatterns = {"/calc/*"}
)
public class InputFilter implements Filter {
    @Override
    public void init(FilterConfig fConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI().substring(6);
        int ct = 0;
        if ("equation".equals(url)) {
            int operators = 0;
            String equation = req.getReader().readLine();
            System.out.println(equation);
            for (int i = 0; i < equation.length(); i++) {
                char elem = equation.charAt(i);
                if ('A' <= elem && elem <= 'Z' ) {
                    resp.setStatus(400);

                    ct++;
                }
                if (elem == '+' || elem == '-' || elem == '/' || elem == '*') {
                    operators++;
                }
            }
            if (operators == 0) {
                resp.setStatus(400);
                ct++;
            }
            req.getReader().reset();
        }
        else {
            String putValue = req.getReader().readLine();
            if (!"".equals(putValue) && putValue != null && (Character.isDigit(putValue.charAt(0)) || putValue.charAt(0) == '-')) {
                if ((Integer.parseInt(putValue) < -10000 || Integer.parseInt(putValue) > 10000)) {
                    resp.setStatus(403);
                    ct++;
                }
            }
            req.getReader().reset();
        }
        if (ct == 0) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
