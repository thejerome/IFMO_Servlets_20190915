package com.sam.web.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Character.isLetter;

@WebFilter(filterName = "ServletFilterEquation",
        urlPatterns = "/calc/equation")
public class EquationFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest reqSer, ServletResponse respSer, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) reqSer;
        HttpServletResponse resp = (HttpServletResponse) respSer;
        String equation = req.getReader().readLine();
        String url = req.getRequestURI().substring(6);
        req.getReader().reset();
        boolean notError = true;
        if ("equation".equals(url)) {
            if (!goodFormatEquation(equation)) {
                notError = false;
                resp.setStatus(400);
                resp.getWriter().write("Неверный формат");
            }
        }
        req.getReader().reset();
        if (notError)
            chain.doFilter(reqSer, respSer);

    }

    private boolean goodFormatEquation(String equation) {
        for (int i = 0; i < equation.length() - 1; i++) {
            if (isLetter(equation.charAt(i)) && isLetter(equation.charAt(i + 1)))
                return false;
        }
        return true;
    }

    @Override
    public void destroy() {
    }
}

