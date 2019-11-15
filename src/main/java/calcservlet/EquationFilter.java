package calcservlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

@WebFilter(filterName = "EquationFilter", urlPatterns = "/calc/equation")

public class EquationFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        BufferedReader reader = request.getReader();
        String qtn = reader.readLine();
        reader.reset();
        if (goodFormat(qtn)) {
            chain.doFilter(req, resp);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("BAD FORMAT");
        }
    }

    public void init(FilterConfig config){

    }
    private boolean goodFormat(String qtn) {
        StringTokenizer tokenizer = new StringTokenizer(qtn, "() ");
        StringBuilder builder = new StringBuilder();
        String q;
        while (tokenizer.hasMoreElements()) {
            builder.append(tokenizer.nextElement());
        }
        q = builder.toString();
        for(int i = 0; i < q.length()-1; i+=2) {
            if((Character.isAlphabetic(q.charAt(i)) || Character.isDigit(q.charAt(i))) &&  "+-*/".contains(String.valueOf(q.charAt(i+1)))) {
                continue;
            }
            return false;
        }
        return Character.isAlphabetic(q.charAt(q.length() - 1));
    }
}
