package calcservlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

@WebFilter(filterName = "ResultFilter", urlPatterns = "/calc/result")
public class ResultFilter implements Filter {

    @Override
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        if(session.getAttribute("equation") != null) {
            String qtn = session.getAttribute("equation").toString();
            if (!goodFormat(qtn)){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if (!hasVars(session)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                chain.doFilter(req, resp);
            }
        }

    }

    @Override
    public void init(FilterConfig config) {
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
            if(!(Character.isAlphabetic(q.charAt(i)) || Character.isDigit(q.charAt(i)) &&  "+-*/".contains(String.valueOf(q.charAt(i+1)))))
            return false;
        }
        return Character.isAlphabetic(q.charAt(q.length() - 1));
    }

    private boolean hasVars(HttpSession session) {
        if(session.getAttribute("equation") != null) {
            String equation = session.getAttribute("equation").toString();
            StringTokenizer tokenizer = new StringTokenizer(equation, "(+-*/)1234567890 ");
            StringBuilder builder = new StringBuilder();
            String q;
            while (tokenizer.hasMoreElements()) {
                builder.append(tokenizer.nextElement());
            }
            q = builder.toString();
            for (char c : q.toCharArray()) {
                if(session.getAttribute(String.valueOf(c)) == null) {
                    return false;
                }
            }
        }
        return true;
    }

}
