package com.gena.ifmo.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(
        urlPatterns = "/calc/equation_not"
)
public class EquationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)throws IOException, ServletException{

        BufferedReader reader = request.getReader();
        String equation = reader.readLine();

        if (equation != null && checkIsFormatBad(equation)){
            //reader.reset();
            request.setAttribute("Problems", "bad format equation");
            //chain.doFilter(request, response);
            //request.getRequestDispatcher("/calc/equation").forward(request,response);
        }
        reader.reset();
        chain.doFilter(request, response);
    }

    private boolean checkIsFormatBad(String equation){
        return Pattern.matches("(\\(*[a-zA-Z]+\\s*([+\\-*/]\\s*[a-zA-Z])+[)(]*)+", equation);

    }

    @Override
    public void destroy(){}
}
