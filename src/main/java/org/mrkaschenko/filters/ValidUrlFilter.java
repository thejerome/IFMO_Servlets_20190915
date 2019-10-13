package org.mrkaschenko.filters;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;


public class ValidUrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Should be empty
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURI().toString();

        System.out.print(url + "  ");
        String[] urlParts = url.split("/");
        String urlToCheck = urlParts[urlParts.length-2] + "/" + urlParts[urlParts.length-1];

        Pattern pattern = Pattern.compile("^calc/[a-z]$");
        Matcher matcher = pattern.matcher(urlToCheck);

        if(matcher.matches()) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //Should be empty
    }
}
