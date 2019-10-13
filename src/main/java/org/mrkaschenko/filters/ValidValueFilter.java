package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import org.mrkaschenko.helpers.Helper;


public class ValidValueFilter implements Filter {

    private static boolean isValid(String value) {
        Pattern pattern = Pattern.compile("^[a-z]$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


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

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();

        if (method.equals("DELETE")){
            chain.doFilter(request, response);
        } else if (Helper.isInteger(requestBody)) {
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

    }
}
