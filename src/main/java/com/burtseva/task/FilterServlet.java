package com.burtseva.task;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FilterServlet implements Filter {

    @Override
    public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        //something
    }

    @Override
    public void init(FilterConfig filterConfig) {
        //something
    }

    @Override
    public void destroy() {
        //something
    }
}
