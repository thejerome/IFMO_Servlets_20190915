package web;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
        filterName = "FilterServlet",
        urlPatterns = {"/calc/equation"}
)

public class FilterServlet implements Filter {
    @Override
    public void init(FilterConfig con) {

    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute("equation") != null){
            chain.doFilter(request,response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy(){

    }

}