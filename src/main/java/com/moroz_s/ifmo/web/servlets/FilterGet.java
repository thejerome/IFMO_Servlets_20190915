package com.moroz_s.ifmo.web.servlets;

import javax.servlet.annotation.WebFilter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter(
        filterName = "Filter_",
        urlPatterns = {"/calc/result"}
)
public class FilterGet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        //it exists and that's enough
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        HttpServletResponse res = (HttpServletResponse) response;


        if (session != null && session.getAttribute("equation") != null){
            chain.doFilter(request, response);
        } else {
            res.setStatus(409);
        }
    }

    @Override
    public void destroy(){
    }
}