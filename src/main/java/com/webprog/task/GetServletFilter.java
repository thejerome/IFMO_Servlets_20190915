package com.webprog.task;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(
        filterName = "GetServletFilter",
        urlPatterns = {"/calc/result"}
)
public class GetServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig)  {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        boolean isOk = true;
        String equation = (String) session.getAttribute("equation");
        if (equation == null){
            isOk = false;
            resp.setStatus(409);
            resp.getWriter().print("Equation is undefined");
        } else {
            for (int i = 0; i<equation.length(); ++i){
                String cur = String.valueOf(equation.charAt(i));
                while (cur.charAt(0) >= 'a' && cur.charAt(0) <= 'z'){
                    cur = (String) session.getAttribute(cur);
                    if (cur == null) {
                        isOk = false;
                        resp.setStatus(409);
                        resp.getWriter().print("Some variable is undefined");
                        break;
                    }
                }
            }
        }
        if (isOk)
            chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

}


