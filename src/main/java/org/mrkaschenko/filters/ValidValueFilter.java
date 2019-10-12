package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import org.mrkaschenko.helpers.Helper;

// @WebFilter(filterName = "ValidValueFilter",
//            servletName = {"/calc/*"})
public class ValidValueFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        BufferedReader in = request.getReader();
        String requestBody = in.readLine();

        Pattern pattern = Pattern.compile("^[a-z]$");
        Matcher matcher = pattern.matcher(requestBody);

        if(Helper.isInteger(requestBody)) {
            if(Integer.parseInt(requestBody) > 10000 || Integer.parseInt(requestBody) < -10000) {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else if(!matcher.matches()) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        request.setAttribute("value", requestBody);
        System.out.print("\nValid Value Filter\n");
        chain.doFilter(request, response);

        System.out.print("Valid Value filter ended");

    }

    @Override
    public void destroy() {

    }
}
