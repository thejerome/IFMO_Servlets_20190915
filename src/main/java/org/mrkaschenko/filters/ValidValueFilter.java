package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import javax.servlet.FilterConfig;
import org.mrkaschenko.helpers.EvalHelper;


public class ValidValueFilter implements Filter {

    private static boolean isValid(String value) {
        Pattern pattern = Pattern.compile("^[a-z]$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Should be empty
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        BufferedReader in = request.getReader();
        String requestBody = in.readLine();

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();

        if ("DELETE".equals(method)){
            chain.doFilter(request, response);
        } else if (EvalHelper.isInteger(requestBody)) {
            if (Integer.parseInt(requestBody) > 10000 || Integer.parseInt(requestBody) < -10000) {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.sendError(403, "bad value");
                return;
            } else {
                request.setAttribute("value", requestBody);
                chain.doFilter(request, response);
            }
        } else if (!isValid(requestBody)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(403, "bad value");
            return;
        } else {
            request.setAttribute("value", requestBody);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //Should be empty
    }
}
