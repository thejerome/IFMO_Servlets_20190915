package com.web.task_second;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
//import javax.servlet.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PressF implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
        throw new ServletException ("");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Matcher matcher = Pattern.compile("[A-Z]+").matcher(request.toString());
        Matcher matcher1 = Pattern.compile("[-+*/]").matcher(request.toString());
        if (!( !matcher.find() && matcher1.find())) {
            throw  new IOException ("");
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
