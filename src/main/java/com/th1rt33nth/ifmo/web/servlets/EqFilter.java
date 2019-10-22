package com.th1rt33nth.ifmo.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "EqFilter",
        urlPatterns = {"/calc/equation"}
)
public class EqFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //codacy, fuck you
    }
    @Override
    public void destroy(){
        //codacy, fuck you
    }
    @Override
    public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String eq = req.getReader().readLine();
        eq=eq.replace(" ", "");
        req.getReader().reset();
        if (eq.indexOf('*') == -1 && eq.indexOf('/') == -1 &&
                eq.indexOf('+') == -1 && eq.indexOf('-') == -1)
            ((HttpServletResponse)resp).setStatus(400);
        else
            chain.doFilter(req,resp);
    }
}