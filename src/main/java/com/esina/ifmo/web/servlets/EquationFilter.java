package com.esina.ifmo.web.servlets;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(
        filterName = "EquationFilter",
        urlPatterns = {"/calc/equation"}
)

public class EquationFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        // yikes! nothing!
    }

    @Override
    public void doFilter (ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        String equation = req.getReader().readLine().replace(" ", "");
        req.getReader().reset();

        if (equation.indexOf('*') == -1 && equation.indexOf('/') == -1 &&
                equation.indexOf('+') == -1 && equation.indexOf('-') == -1)
            ((HttpServletResponse)resp).setStatus(400);
        else
            chain.doFilter(req,resp);
    }

    @Override
    public void destroy(){
        // yikes! nothing!
    }

}