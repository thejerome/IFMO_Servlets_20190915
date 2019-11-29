package com.mishep.web.utils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(
        filterName = "GetServletFilter",
        urlPatterns = {"/calc/result"}
)
public class GetServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)  { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            if ((((HttpServletRequest) request).getSession()).getAttribute("equation") != null){
                chain.doFilter(request,response);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() { }
}


