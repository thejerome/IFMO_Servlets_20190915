package com.marycia.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter (
        filterName = "ReqFilter",
        urlPatterns = {"/calc/*"}
)

public class ReqFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //codacy doesn't want to see the empty method
    }

    @Override
    public void doFilter (ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        /* if ("equation".equals(value)) {
            String key = req.getReader().readLine();
            boolean consistOfDel = false;
            for (int i = 0; i < key.length(); i++) {
                if (key.charAt(i) == '/' || key.charAt(i) == '*' || key.charAt(i) == '-' ||  key.charAt(i) == '+' || key.charAt(i) == '(' || key.charAt(i) == ')')
                consistOfDel = true;
            }
            if (!consistOfDel) {
                resp.setStatus(400);
                resp.getWriter().print("Bad format. Try again");
                GetError = true;
            } else {
                GetError = false;
            }
        } */

        chain.doFilter(sreq,sresp);


    }

    public void destroy(){
        //codacy doesn't want to see the empty method
    }

}
