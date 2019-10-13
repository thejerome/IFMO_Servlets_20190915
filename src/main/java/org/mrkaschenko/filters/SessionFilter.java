package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpSession;


public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession oldSession = req.getSession(false);
        if(oldSession == null) {
            HttpSession newSession = req.getSession(true);
            newSession.setMaxInactiveInterval(5*60);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
