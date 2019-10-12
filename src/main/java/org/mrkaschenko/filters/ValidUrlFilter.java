package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.mrkaschenko.helpers.Helper;

// @WebFilter(filterName = "ValidUrlFilter",
//            urlPatterns = {"/calc/*"})
public class ValidUrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        String url = ((HttpServletRequest)request).getRequestURI().toString();
        System.out.print(url + "  ");
        String[] urlParts = url.split("/");
        String urlToCheck = urlParts[urlParts.length-2] + "/" + urlParts[urlParts.length-1];

        Pattern pattern = Pattern.compile("^calc/[a-z]$");
        Matcher matcher = pattern.matcher(urlToCheck);
        RequestDispatcher rdObj = null;

        if(matcher.matches()) {
            System.out.print("setValue or delValue via put or delete");
            System.out.print("-----------------------------------------");
            chain.doFilter(request, response);
        }
        System.out.print("Valid Url filter ended");

        // final HttpSession session = request.getSession(false);
        // if (session == null) {
        //
        // }

        // final String name = request.getParameter("name");
        // if (name == null) {
        //     request.getRequestDispatcher("/not_polite")
        //         .forward(request, response);
        // }


    }

    @Override
    public void destroy() {

    }
}
