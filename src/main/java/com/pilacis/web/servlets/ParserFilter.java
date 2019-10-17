package com.pilacis.web.servlets;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "filter",
        urlPatterns = {"/calc/*"}
)
public class ParserFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

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

        }
        else{

            // url = имя , reader - значение
            HttpSession session = req.getSession();
            final String variableName = req.getRequestURI().substring(6);
            String valueStr = req.getReader().readLine();
            req.getReader().reset();


            if (valueStr != null) {
                while ((valueStr.charAt(0) >= 'a') && (valueStr.charAt(0) <= 'z')) {
                    System.out.println(valueStr);
                    valueStr = (String) session.getAttribute(valueStr);
                }
                int value = Integer.valueOf(valueStr);


                if ((Integer.valueOf(value) < -10000) || (Integer.valueOf(value) > 10000)) {
                    statusCode = 403;
                }
            }
            else{
                statusCode = 204;
                System.out.println(200000);
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

    }
}
