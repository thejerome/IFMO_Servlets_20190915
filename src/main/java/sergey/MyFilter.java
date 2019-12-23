package sergey;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(
        urlPatterns = {"/calc/*"}
)
public class MyFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig arg0) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}