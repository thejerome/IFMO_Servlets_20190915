package com.gena.ifmo.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(
        urlPatterns = "/calc/*"
)
public class ArgumentsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse response,
            FilterChain chain)throws IOException, ServletException{

        HttpServletRequest request = (HttpServletRequest)req;

        BufferedReader reader = request.getReader();
        String argumentValue = reader.readLine();

        String thisPath = request.getRequestURI();
        String argumentName = thisPath.substring(thisPath.lastIndexOf('/') + 1);

        if (!"equation".equals(argumentName) && checkIsVariableFormatBad(argumentValue)){
            request.setAttribute("403", "bad format variable");
        }
        if ("equation".equals(argumentName) && (checkIsEquationFormatBad(argumentValue) || checkIsParenthesesBad(argumentValue))){
            request.setAttribute("400", "bad format equation");
        }
        reader.reset();
        chain.doFilter(request, response);
    }

    private boolean checkIsVariableFormatBad(String variable){
        if (variable == null) return true;
        if (Pattern.matches("-*\\d+" , variable)){
            return !(-10000 < Integer.parseInt(variable) && Integer.parseInt(variable) < 10000);
        }else{
            return !Pattern.matches("[a-zA-Z]+" ,variable);
        }

    }

    private boolean checkIsEquationFormatBad(String equation){
        return !Pattern.matches("[(\\da-zA-Z]+\\s*([+\\-*/]\\s*[()\\da-zA-Z]+\\s*)+", equation);
    }



    private boolean checkIsParenthesesBad(String equation){
        int nomberOfOpenedParentheses = 0;
        for(int i = 0; i < equation.length(); i++){
            if (equation.charAt(i) == '(') nomberOfOpenedParentheses++;
            if (equation.charAt(i) == ')') nomberOfOpenedParentheses--;
            if (nomberOfOpenedParentheses < 0) return true;
        }
        return (nomberOfOpenedParentheses != 0);
    }

    @Override
    public void destroy(){

    }
}
