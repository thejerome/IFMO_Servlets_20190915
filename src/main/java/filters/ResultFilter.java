package filters;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.StringTokenizer;

@WebFilter(filterName = "ResultFilter", urlPatterns = "/calc/result")
public class ResultFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("equation") != null) {
            String temp = session.getAttribute("equation").toString();
            if (!isGood(temp)) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if (!hasVars(session)) {
                httpResponse.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig config) {

    }

    private boolean isGood(String toCheck) {
        StringTokenizer stringTokenizer = new StringTokenizer(toCheck, "() ");
        StringBuilder temp = new StringBuilder();
        String res;
        while (stringTokenizer.hasMoreElements()) {
            temp.append(stringTokenizer.nextElement());
        }
        res = temp.toString();
        for (int i = 0; i < res.length() - 1; i += 2) {
            if (!(
                    (Character.isAlphabetic(res.charAt(i)) ||
                            Character.isDigit(res.charAt(i))) &&
                            "+-*/".contains(String.valueOf(res.charAt(i + 1)))
            ))
                return false;
        }
        return Character.isAlphabetic(res.charAt(res.length() - 1));
    }

    private boolean hasVars(HttpSession toCheck) {
        if (toCheck.getAttribute("equation") == null) {
            return true;
        }
        String equation = toCheck.getAttribute("equation").toString();
        StringTokenizer stringTokenizer = new StringTokenizer(equation, "(+-*/)1234567890 ");
        StringBuilder temp = new StringBuilder();
        String res;
        while (stringTokenizer.hasMoreElements()) {
            temp.append(stringTokenizer.nextElement());
        }
        res = temp.toString();
        for (char c : res.toCharArray()) {
            if (toCheck.getAttribute(String.valueOf(c)) == null) {
                return false;
            }
        }
        return true;
    }
}