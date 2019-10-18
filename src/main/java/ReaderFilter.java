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
        filterName = "ReaderFilter",
        urlPatterns = {"/calc/result"}
)

public class ReaderFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void doFilter (ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)sreq;
        HttpSession sess = req.getSession();
        if (sess.getAttribute("equation") != null){
            chain.doFilter(sreq,sres);
        }
    }
    @Override
    public void destroy() {}
}
