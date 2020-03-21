package com.misha.task2;

import javax.servlet.Filter;
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

@WebFilter(filterName = "Task2Filter", urlPatterns = "/calc/*")
public class Task2Filter implements Filter {
    public void destroy() {
        // empty function
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        if ("GET".equals(request.getMethod())){
            if (session.getAttribute("equation") == null) {
                response.setStatus(409);
                resp.getWriter().print("Lack of data");
            } else {
                String equation = session.getAttribute("equation").toString().replace(" ", "");
                StringTokenizer stringTokenizer = new StringTokenizer(equation, "+-*/1234567890()");
                while (stringTokenizer.hasMoreElements()) {
                    String nextToken = stringTokenizer.nextToken();
                    if (session.getAttribute(nextToken) == null) {
                        response.setStatus(409);
                        resp.getWriter().print("Lack of data");
                    }
                }
            }
            if (response.getStatus() != 409) {
                chain.doFilter(req, resp);
            }
        } else if ("PUT".equals(request.getMethod())) {
            String pathInfo = request.getPathInfo().substring(1);
            String body = request.getReader().readLine().replace(" ", "");
            request.getReader().reset();
            if ("equation".equals(pathInfo)) {
                for (char c : body.toCharArray()) {
                    if (String.valueOf(c).matches("[(+\\-*/)]")) {
                        chain.doFilter(req, resp);
                        return;
                    }
                }
                response.setStatus(400);
            } else {
                if (body.length() == 1 && Character.isAlphabetic(body.charAt(0))) {
                    chain.doFilter(req, resp);
                } else {
                    int intValue = Integer.parseInt(body);
                    if (intValue >= -10000 && intValue <= 10000) {
                        chain.doFilter(req, resp);
                    } else {
                        response.setStatus(403);
                        resp.getWriter().print("Value exceeds");
                    }
                }
            }
        } else if ("DELETE".equals(request.getMethod())) {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) {
        // empty function
    }

}
