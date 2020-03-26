package task2;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "FilterKek",
        urlPatterns = "/calc/equation"
)
public class FilterKek implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(filterConfig.toString());
        //empty
    }

    @Override
    public void doFilter(ServletRequest reqq, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) reqq;
        HttpServletResponse res = (HttpServletResponse) resp;
        boolean badformat = false;
        String equation = req.getReader().readLine();
        System.out.println(equation);
        if (!check(equation)) {
            res.setStatus(400);
            res.getOutputStream().write("bad".getBytes());
            res.getOutputStream().flush();
            badformat = true;
        }
        req.getReader().reset();

        if (!badformat)
            chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("Destroy");
        //empty
    }

    private boolean check(String eq) {
        boolean op = false;
        for (char c : eq.toCharArray()) {
            if (c >= 'A' && c <= 'Z')
                return false;
            if (c == '+' || c == '-' || c == '/' || c == '*')
                op = true;
        }
        return op;
    }
}
