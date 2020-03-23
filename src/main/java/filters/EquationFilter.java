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
import java.io.*;
import java.util.StringTokenizer;

@WebFilter(filterName = "EquationFilter", urlPatterns="/calc/equation")
public class EquationFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        BufferedReader reader = httpRequest.getReader();
        String temp = reader.readLine();
        reader.reset();
        if (isGood(temp)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void destroy() {
        // something goes here
    }

    @Override
    public void init(FilterConfig config) {
        // something goes here
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
}