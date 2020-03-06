package servletstask2;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/calc/*")
public class ResultFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            doFilter((HttpServletRequest) req, (HttpServletResponse) resp, chain);
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {}

    private void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (req.getPathInfo().split("/")[1].equals("result")) {
            if (req.getMethod().equalsIgnoreCase("get")) {
                chain.doFilter(req, resp);
            }
        } else {
            if (!req.getMethod().equalsIgnoreCase("get")) {
                chain.doFilter(req, resp);
            }
        }
    }
}
