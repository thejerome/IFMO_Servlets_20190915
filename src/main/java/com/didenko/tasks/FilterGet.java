package com.didenko.tasks;


import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
        filterName = "Filter_",
        urlPatterns = {"/calc/result"}
)
public class FilterGet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        //q
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        HttpServletResponse res = (HttpServletResponse) response;
        if (session == null){
            res.setStatus(409);
        } else {
            if (session.getAttribute("equation") == null){
                res.setStatus(409);
            } else {
                chain.doFilter(request,response);
            }
        }
    }

    @Override
    public void destroy() {
        //q
    }
}
