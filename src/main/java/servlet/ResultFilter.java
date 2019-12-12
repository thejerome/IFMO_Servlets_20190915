package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.Filter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

@WebFilter(filterName = "ResultFilter", urlPatterns = "/calc/result")
public class ResultFilter implements Filter {
    public void destroy() {
        //there could be destroy code, but ...
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        PrintWriter out = resp.getWriter();
        HttpSession currSession = request.getSession();
        if (currSession.getAttribute("expr") == null) {
            response.setStatus(409);
            out.println("I don't have expression to evaluate!");
            System.out.println("[!] ERROR: Equation doesn't exist!");
        } else {
            String expr = currSession.getAttribute("expr").toString();
            StringTokenizer tokenizer = new StringTokenizer(expr, "(+-*/)0123456789 ");
            while (tokenizer.hasMoreTokens()) {
                if (currSession.getAttribute(tokenizer.nextToken()) == null) {
                    response.setStatus(409);
                    out.println("Lack of data!");
                    System.out.println("[!] ERROR: Lack of data!");
                    break;
                }
            }
        }
        if (response.getStatus() != 409) {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) {
        //there could be init code, but ...
    }

}
