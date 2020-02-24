package filters;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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