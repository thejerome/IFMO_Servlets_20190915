package calc;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

@WebFilter(filterName = "CalcFilter", servletNames = "CalcServlet")
public class Filter implements javax.servlet.Filter {
    public void destroy() {
        //empty
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String method = request.getMethod();
        switch (method) {
            case "PUT":
                String pathInfo = request.getPathInfo();
                String line = request.getReader().readLine();
                request.getReader().reset();
                if ("/equation".equals(pathInfo)) {
                    if (isValidEquation(line)) {
                        chain.doFilter(req, resp);
                    } else {
                        resp.getWriter().println("Bad format");
                        response.setStatus(400);
                    }
                } else {
                    try {
                        int valueInt = Integer.parseInt(line);
                        if (valueInt < -10000 || valueInt > 10000) {
                            resp.getWriter().println("Exceeding values");
                            response.setStatus(403);
                        } else {
                            chain.doFilter(req, resp);
                        }
                    } catch (NumberFormatException ignored) {
                        if (line.length() == 1 && Character.isAlphabetic(line.charAt(0))) {
                            chain.doFilter(req, resp);
                        } else {
                            resp.getWriter().println("Bad format");
                            response.setStatus(400);
                        }
                    }
                }
                break;
            case "GET":
                if (session.getAttribute("equation") != null) {
                    String equation = session.getAttribute("equation").toString();
                    StringTokenizer stringTokenizer = new StringTokenizer(equation, "0123456789()+-*/");
                    while (stringTokenizer.hasMoreElements()) {
                        if (session.getAttribute(stringTokenizer.nextToken()) == null) {
                            resp.getWriter().println("Expression may not be calculated due to lack of data");
                            response.setStatus(409);
                            break;
                        }
                    }
                } else {
                    resp.getWriter().println("No expression to evaluate");
                    response.setStatus(409);
                }
                if (response.getStatus() != 409)
                    chain.doFilter(req, resp);
                break;
            case "DELETE":
                chain.doFilter(req, resp);
                break;
            default:
                response.setStatus(405);
        }
    }

    private boolean isValidEquation(String equation) {
        Calc calc = new Calc();
        return calc.isCorrectEquation(equation);
    }

    public void init(FilterConfig config) {
        //empty
    }
}
