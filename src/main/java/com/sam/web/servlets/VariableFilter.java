package com.sam.web.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Character.isLetter;

@WebFilter(filterName = "ServletFilterVariable",
        urlPatterns = "/calc/*")
public class VariableFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest reqSer, ServletResponse respSer, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) reqSer;
        HttpServletResponse resp = (HttpServletResponse) respSer;
        String url = req.getRequestURI().substring(6);
        req.getReader().reset();
        boolean notError = true;

        if ((!"result".equals(url)) && (!"equation".equals(url))){
            String value = req.getReader().readLine();
            req.getReader().reset();
            if ((value != null) && (!goodFormatValue(value))) {
                resp.setStatus(403);
                notError = false;
            }
        }
        req.getReader().reset();
        if (notError)
            chain.doFilter(reqSer, respSer);
    }
    @Override
    public void destroy() {}

    static private boolean goodFormatValue(String value) {
        Character symbol = value.charAt(0);
        if (isLetter(symbol) && value.length() == 1)
            return true;
        try {
            int a = Integer.parseInt(value);
            if (a < -10000 || a > 10000){
                return false;
            }
            else {
                return true;
            }
        } catch (Exception e){
            return true;
        }
    }
}

