package Lab2;

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
        filterName = "IntFilter",
        urlPatterns = {"/calc/result"}
)
public class IntFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //aaa
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        if (session == null)
            resp.setStatus(409);
        else
            chain.doFilter(request, response);

    }



    @Override
    public void destroy() {
        //aaaa
    }
}