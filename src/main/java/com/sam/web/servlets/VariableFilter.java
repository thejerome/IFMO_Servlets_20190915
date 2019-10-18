package com.sam.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Character.isLetter;

@WebFilter(filterName = "ServletFilterVariable",
        urlPatterns = "/calc/*")
public class VariableFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }


    @Override
    public void doFilter(ServletRequest reqSer, ServletResponse respSer, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) reqSer;
        HttpServletResponse resp = (HttpServletResponse) respSer;
        String url = String.valueOf(req.getRequestURI().charAt(6));
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
    public void destroy() {
        //
    }

    private boolean goodFormatValue(String value) {
        Character symbol = value.charAt(0);
        if (isLetter(symbol) && value.length() == 1)
            return true;
        try {
            int a = Integer.parseInt(value);
            return Math.abs(a) <= 10000;
        } catch (Exception exp){
            return true;
        }
    }
}

