package org.mrkaschenko.filters;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;


public class ValidEquationFilter implements Filter {

    private static boolean isExpression(String s) {
        Pattern pattern = Pattern.compile("^[(]*[a-z0-9]?([-+/*][(]*[a-z0-9][)]*)*$");
        Matcher matcher = pattern.matcher(s);
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
        requestBody = requestBody.replaceAll("\\s","");

        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod();

        if (method.equals("DELETE")){
            chain.doFilter(request, response);
        } else if(!isExpression(requestBody)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(400, "equation bad format");
        } else {
            request.setAttribute("equation", requestBody);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
