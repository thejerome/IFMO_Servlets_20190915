package com.piskov.web.servlets;


import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(
        urlPatterns = "/calc/*"
)
public class ExpressionFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {
        //null
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String reqURL = req.getRequestURI().substring(6);
        int status=0;
            if ("equation".equals(reqURL)) {
                String equation = req.getReader().readLine();
                req.getReader().reset();
                if (isNotExpression(equation)) {
                    status=400;
                    res.getWriter().write("Bad FORMAT");
                }
            }
            else if (!"result".equals(reqURL)) {
                String value = req.getReader().readLine();
                req.getReader().reset();
                    if (value != null &&!isInRange(value)) {
                        status = 403;
                    }
            }
        req.getReader().reset();


        if (status == 0)
            chain.doFilter(request, response);
        else
            res.setStatus(status);
    }

    private static boolean isNotExpression(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('*' <= expression.charAt(i) && '/' >= expression.charAt(i)) return false;
        }
        return true;
    }

    private static boolean isInRange(String value){
            if (value.charAt(0) >= 'a' && value.charAt(0) <= 'z')
                return true;
            else
                return (Integer.parseInt(value) <= 10000 && Integer.parseInt(value) >= -10000) ;
    }

    @Override
    public void destroy() {
        //null
    }
}
