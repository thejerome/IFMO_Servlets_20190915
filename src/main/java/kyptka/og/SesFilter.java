package kyptka.og;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "SesFilter",
           urlPatterns = {"/calc/equation"})
public class SesFilter implements Filter {
    @Override
    public void init(FilterConfig con) {
        //e m p t y
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest servReq = (HttpServletRequest) req;
        servReq.getSession(true);
        chain.doFilter(req, resp);
    }

    public void destroy(){
        //e m p t y
    }

}
