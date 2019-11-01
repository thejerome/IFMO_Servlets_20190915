package com.dorofeeva;

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
// а как каркать

public class EqFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //пук
    }

    @Override
    public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        String equation = req.getReader().readLine();

        equation = equation.replace(" ", "");

        req.getReader().reset();

        if (ck(equation)){
            ((HttpServletResponse)resp).setStatus(400);
        }
        else
            chain.doFilter(req,resp);
    }
    private boolean ck (String s){
        boolean f = true;
        if (s.contains("+")||s.contains("-")){
            f = false;
        }
        if (s.contains("*")||s.contains("/")){
            f= false;
        }
        return f;
    }

    @Override
    public void destroy(){
        //пук
    }
}