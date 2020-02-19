package filters;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import java.io.IOException;
import java.io.BufferedReader;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;

import helpers.SomeHelper;


@WebFilter(servletNames = "ValueServlet")
public class ValueFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //Should be empty
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession oldSession = req.getSession(false);
        if (oldSession == null) {
            HttpSession newSession = req.getSession(true);
            newSession.setMaxInactiveInterval(360);
        }

        BufferedReader in = request.getReader();
        String requestBody = in.readLine();

        String method = req.getMethod();
        if ("DELETE".equals(method)) {
            chain.doFilter(request, response);
            return;
        }

        if (SomeHelper.isInteger(requestBody)) {
            if (Integer.parseInt(requestBody) > 10000 || Integer.parseInt(requestBody) < -10000) {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.sendError(403, "value is too big");
            } else {
                request.setAttribute("value", requestBody);
                chain.doFilter(request, response);
            }
        } else if (!SomeHelper.isValue(requestBody)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(403, "invalid value");
        } else {
            request.setAttribute("value", requestBody);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //Should be empty
    }
}
