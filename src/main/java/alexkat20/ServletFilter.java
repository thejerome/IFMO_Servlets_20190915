package alexkat20;


import javax.servlet.FilterConfig;
import javax.servlet.Filter;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;


@WebFilter( urlPatterns = {"/calc/result"})

public class ServletFilter implements Filter{
    public void init(FilterConfig filterConfig){//jhhv
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpSession session = request1.getSession();
        if (session.getAttribute("equation") != null) {
            chain.doFilter(request, response);
        }
    }
    @Override
    public void destroy(){
        //hf
    }
}
