package servlets;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter("/calc/result")
public class CalcFilter implements Filter {

    private static final Pattern VARIABLE_REGEX = Pattern.compile("[a-z]");

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        filterConfig.getServletContext().log(String.format("%s initialized", filterConfig.getFilterName()));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            this.doHttpFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void doHttpFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (!"GET".equals(req.getMethod())) {
            resp.sendError(400);
            return;
        }

        String eq = Optional.ofNullable(req.getSession().getAttribute("equation"))
                .map(Object::toString).orElse(null);

        if (eq == null) {
            resp.sendError(409, "Undefined equation");
            return;
        }

        req.getSession().setAttribute("newEquation", eq);
        for (Matcher matcher = VARIABLE_REGEX.matcher(eq); matcher.find(); matcher = VARIABLE_REGEX.matcher(eq)) {
            final int start = matcher.start();
            final int end = matcher.end();

            final Object param = req.getSession().getAttribute(eq.substring(start, end));
            if (param == null) {
                resp.sendError(409, "Unknown parameter");
                return;
            }

            eq = (eq.substring(0, start) + param + (end == eq.length() - 1 ? "" : eq.substring(end)))
                    .replaceAll("\\s", "");
        }

        req.getSession().setAttribute("equation", eq);
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        filterConfig.getServletContext().log(String.format("%s destroyed", filterConfig.getFilterName()));
        filterConfig = null;
    }
}
