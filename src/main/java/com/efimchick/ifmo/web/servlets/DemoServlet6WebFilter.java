package com.efimchick.ifmo.web.servlets;

/**
 * Created by EE on 2018-11-01.
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(filterName = "demo6filter",
    urlPatterns = {"/demo6"},
    initParams = {@WebInitParam(name = "mood", value = "awake")})
public class DemoServlet6WebFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        final String name = request.getParameter("name");
        if (name == null) {
            request.getRequestDispatcher("/not_polite")
                .forward(request, response);
        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
