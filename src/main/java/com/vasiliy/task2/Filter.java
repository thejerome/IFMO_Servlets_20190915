package com.vasiliy.task2;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

@WebFilter(filterName = "CalcFilter", urlPatterns = "/calc/*")
public class Filter implements javax.servlet.Filter {
    public void destroy() {
        // --
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession requestSession = request.getSession();
        String requestPathInfo = request.getPathInfo().substring(1);
        if ("PUT".equals(request.getMethod())) {
            String reqBody = req.getReader().readLine().replace(" ", "");
            req.getReader().reset();
            if ("equation".equals(requestPathInfo)) {
                for (char c : reqBody.toCharArray()) {
                    if (String.valueOf(c).matches("[()+\\-*/]")) {
                        chain.doFilter(req, resp);
                        return;
                    }
                }
                response.setStatus(400);
            } else {
                if (reqBody.length() == 1 && Character.isAlphabetic(reqBody.charAt(0))) {
                    chain.doFilter(req, resp);
                } else {
                    int intValue = Integer.parseInt(reqBody);
                    if (intValue <= 10000 && intValue >= -10000) {
                        chain.doFilter(req, resp);
                    } else {
                        response.setStatus(403);
                        resp.getWriter().print("Value exceeds");
                    }
                }
            }
        } else if ("DELETE".equals(request.getMethod())) {
            chain.doFilter(req, resp);
        } else if ("GET".equals(request.getMethod())){
            if (requestSession.getAttribute("equation") == null) {
                response.setStatus(409);
                resp.getWriter().print("Lack of data");
            } else {
                String equation = requestSession.getAttribute("equation").toString().replace(" ", "");
                StringTokenizer stringTokenizer = new StringTokenizer(equation, "+-*/1234567890()");
                while (stringTokenizer.hasMoreElements()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (requestSession.getAttribute(nextToken) == null) {
                        response.setStatus(409);
                        resp.getWriter().print("Lack of data");
                    }
                }
            }
            if (response.getStatus() != 409) {
                chain.doFilter(req, resp);
            }
        }
    }

    public void init(FilterConfig config) {
        // --
    }

}
