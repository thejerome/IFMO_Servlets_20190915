import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "inputFilter",
        urlPatterns = {"/calc/equation"}
)

public class ServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Миша сказал, что так будет работать
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String equation = req.getReader().readLine();
        boolean containsMathSymbols = false;
        boolean containsUpperCase = false;
        for (char c: equation.toCharArray()) {
            if (Character.toString(c).matches("[A-Z]")){
                containsUpperCase = true;
            } else if (Character.toString(c).matches("[-+*/]")) {
                containsMathSymbols = true;
            }
        }
        req.getReader().reset();
        if (containsMathSymbols && !containsUpperCase) {
            resp.setStatus(200);
            chain.doFilter(request, response);
        } else {
            resp.setStatus(400);
        }
    }

    @Override
    public void destroy() {
        //Виталя сказал, что так будет работать
    }
}
