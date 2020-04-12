package com.slavalapin.ifmo.web.servlets;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

@WebFilter(
        filterName = "ExpressionFilter",
        urlPatterns = {"/calc/equation"}
)


public class ExpressionFilter implements Filter {
    @Override
    public void init(FilterConfig config) {
        // I have no functionality to add
    }

    @Override
    public void destroy() {
        //Nothing still
    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        String expression = request.getReader().readLine();
        request.getReader().reset();

        Pattern mathExpressionRegExp = Pattern.compile(".*[*/+-]+.*");
        Matcher matcherBodyRegExp = mathExpressionRegExp.matcher(expression);

        if (!matcherBodyRegExp.matches())
            ((HttpServletResponse)response).setStatus(400);
        else
            filterChain.doFilter(request,response);
    }
}
