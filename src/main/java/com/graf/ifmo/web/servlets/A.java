package com.graf.ifmo.web.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "A",
        urlPatterns = {"/calc/equation"}
)

public class A implements Filter {
    @Override
    public void init(FilterConfig con) {
        //im not empty, stupid codacy
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        String equation = req.getReader().readLine().replace(" ", "");
        req.getReader().reset();

        if (equation.indexOf('*') == -1 && equation.indexOf('/') == -1 && equation.indexOf('+') == -1 && equation.indexOf('-') == -1)
            ((HttpServletResponse) resp).setStatus(400);
        else
            chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        //you're still here? huh
    }
}