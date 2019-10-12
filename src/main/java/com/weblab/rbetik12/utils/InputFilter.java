package com.weblab.rbetik12.utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(
        filterName = "InputFilter",
        urlPatterns = {"/calc/*"}
)
public class InputFilter implements Filter {
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
            Matcher matcher = Pattern.compile("[A-Z]+").matcher(equation);
            Matcher matcher1 = Pattern.compile("[-+*/]").matcher(equation);
            if (matcher.find() || !matcher1.find()) {
                res.setStatus(400);
                res.getWriter().write("Bad formatted");
                error = true;
            }
            req.getReader().reset();
        } else if (Pattern.matches("[a-z]", reqURL) && !reqURL.equals("result")) {
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

    @Override
    public void destroy() {

    }
}
