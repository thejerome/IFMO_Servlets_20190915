package filters;

import javax.servlet.Filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "ExpressionFilter", urlPatterns = {"/calc/equation"})
public class ExpressionFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //nothing
    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletRequest.getSession(true);
        chain.doFilter(request, response);
    }

    public void destroy(){
        //nothing
    }

}