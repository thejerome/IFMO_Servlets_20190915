package com.ogalay.task.web;

import javax.servlet.FilterConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;


@WebFilter( urlPatterns = {"/calc/result"})

public class ServletFilter implements javax.servlet.Filter {

    public void init(FilterConfig filterConfig){
        //forCommit
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpSession session = request1.getSession();
        String eq = (String) session.getAttribute("equation");
        if (eq != null) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy(){
        //uikiumynh
    }
}

