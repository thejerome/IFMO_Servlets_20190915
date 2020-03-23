package calculator;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "LFilter",
        urlPatterns = {"/calc/*"}
)
public class LFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI().substring(6);
        boolean badformat = false;
        if ("equation".equals(uri)) {
            String equation = req.getReader().readLine();

            if (!checkEquation(equation)) {
                res.setStatus(400);
                res.getOutputStream().write("format".getBytes());
                badformat = true;
            }
            req.getReader().reset();
        } else if (!"result".equals(uri) && containsLetter(uri)) {
            try {
                String rbody = req.getReader().readLine();
                int value = Integer.parseInt(rbody);
                if (value > 10000 || value < -10000) {
                    res.setStatus(403);
                    badformat = true;
                }
            } catch (NumberFormatException ignored) {
            } finally {
                req.getReader().reset();
            }
        }


        if (!badformat) {
            chain.doFilter(request, response);
        }

    }

    private boolean checkEquation(String eq) {
        boolean op = false;
        for (char c : eq.toCharArray()) {
            if (c >= 'A' && c <= 'Z')
                return false;
            if (c == '+' || c == '-' || c == '/' || c == '*')
                op = true;
        }
        return op;
    }

    private boolean containsLetter(String str) {
        return str.charAt(0) >= 'a' && str.charAt(0) <= 'z';
    }

    @Override
    public void destroy() {
        //empty
    }
}
