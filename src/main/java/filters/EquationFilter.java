package filters;

import helpers.SomeHelper;

import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.BufferedReader;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpSession;


@WebFilter(servletNames = "EquationServlet")
public class EquationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //Should be empty
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession oldSession = req.getSession(false);
        if(oldSession == null) {
            HttpSession newSession = req.getSession(true);
            newSession.setMaxInactiveInterval(360);
        }

        BufferedReader in = request.getReader();
        String requestBody = in.readLine();
        requestBody = requestBody.replaceAll("\\s","");

        if (req.getMethod().equals("DELETE")){
            chain.doFilter(request, response);
        } else if(!SomeHelper.isExpression(requestBody)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(400, "equation bad format");
        } else {
            request.setAttribute("equation", requestBody);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        //Should be empty
    }
}
