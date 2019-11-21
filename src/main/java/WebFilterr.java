import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.Filter;
import java.io.PrintWriter;

@javax.servlet.annotation.WebFilter(filterName = "demofilter",
        urlPatterns = {"/calc/result"})

public class WebFilterr implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();
        if (session.getAttribute("equation") != null) {
            char[] modequation = ((String) session.getAttribute("equation")).toCharArray();
            for (char c : modequation)
                if (Character.isLetter(c) && session.getAttribute(String.valueOf(c)) == null) {
                    res.setStatus(409);
                    out.print("Result can't be evaluated due to lack of data.");
                    return;
                }
            chain.doFilter(request, response);
        } else {
            res.setStatus(409);
            out.print("Result can't be evaluated due to lack of data.");
            return;
        }
    }
    @Override
    public void destroy() {

    }
}
