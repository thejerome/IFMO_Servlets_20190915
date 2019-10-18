package com.web.task;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FilterServlet implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //void isn't empty
    }

    @Override
    public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain) {
        //this void isn't empty too
    }

    @Override
    public void destroy() {
        //I created this class because it's useful
    }
}
