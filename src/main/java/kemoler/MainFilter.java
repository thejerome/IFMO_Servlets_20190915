package kemoler;

import javax.servlet.Filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "MainFilter", urlPatterns = {"/calc/equation"})
public class MainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        httpServletRequest.getSession(true);
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        //
    }
}

