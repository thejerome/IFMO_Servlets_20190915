package servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "VariableFilter", urlPatterns = "/calc/*")
public class VariableFilter implements Filter {
    public void destroy() {
        //there could be destroy code, but ...
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;
        String value = req.getReader().readLine();
        req.getReader().reset();
        String path = request.getRequestURI();
        if (!"/calc/equation".equals(path) && !"/calc/result".equals(path)) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue < -10000 || intValue > 10000) {
                    response.setStatus(403);
                } else {
                    chain.doFilter(req, resp);
                }
            } catch (NumberFormatException nfe) {
                if (value != null) {
                    if (value.length() == 1 && Character.isAlphabetic(value.charAt(0))) {
                        chain.doFilter(req, resp);
                    }
                } else {
                    if(!"DELETE".equals(request.getMethod())){
                        System.out.println("[X] Bad variable format.");
                        response.setStatus(400);
                    } else {
                        chain.doFilter(req, resp);
                    }
                }
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) {
        //there could be init code, but ...
    }

}
