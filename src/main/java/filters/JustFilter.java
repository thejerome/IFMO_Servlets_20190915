package filters;


import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;

@WebFilter(
        urlPatterns = {"/calc/result"}
)

public class JustFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        //some comment
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        if (session.getAttribute("equation") != null){
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {
        // something strange
    }
}
