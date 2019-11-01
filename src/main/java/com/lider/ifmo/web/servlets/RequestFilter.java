package com.lider.ifmo.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter (
        filterName = "RequestFilter",
        urlPatterns = {"/calc/*"}
)
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //codacy doesn't want to see the empty method
    }

    @Override
    public void doFilter (ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(sreq,sresp);
    }

    public void destroy(){
        //codacy doesn't want to see the empty method
    }

}