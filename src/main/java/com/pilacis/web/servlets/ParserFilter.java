package com.pilacis.web.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "filter",
        urlPatterns = {"/calc/*"}
)
public class ParserFilter implements Filter {
    public void init(FilterConfig filterConfig){
        //some code
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        int statusCode = -1;



        if (req.getRequestURI().substring(6).compareTo("equation") == 0){
            final String equation = req.getReader().readLine();
            req.getReader().reset();

            if ((equation.indexOf('+') == -1) && (equation.indexOf('/') == -1) &&
                (equation.indexOf('/') == -1) && (equation.indexOf('*') == -1)){
                statusCode = 400;
            }
        }
        else if (req.getRequestURI().substring(6).compareTo("result") == 0){
            HttpSession session = req.getSession();
            if (session == null){
                statusCode = 409;
            }
        }
        else{

            // url = имя , reader - значение
            HttpSession session = req.getSession();
            String valueStr = req.getReader().readLine();
            req.getReader().reset();


            if (valueStr != null) {
                while ((valueStr.charAt(0) >= 'a') && (valueStr.charAt(0) <= 'z')) {

                    valueStr = (String) session.getAttribute(valueStr);
                }
                int value = Integer.valueOf(valueStr);


                if ((Integer.valueOf(value) < -10000) || (Integer.valueOf(value) > 10000)) {
                    statusCode = 403;
                }
            }
            else{
                statusCode = 204;

            }

        }

        if (statusCode != -1) {
            if (statusCode < 300) {
                res.setStatus(statusCode);
                chain.doFilter(request, response);
            } else {
                res.setStatus(statusCode);
            }
        }
        else{
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        //some code
    }
}
