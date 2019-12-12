package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@WebFilter(filterName = "EquationFilter", urlPatterns = "/calc/equation")
public class EquationFilter implements Filter {
    public void destroy() {
        //there could be destroy code, but ...
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String expression = req.getReader().readLine();
        req.getReader().reset();
        if(checkParentheses(expression) && checkExpr(expression)) {
            chain.doFilter(req, resp);
        } else {
            HttpServletResponse response = (HttpServletResponse) resp;
            response.setStatus(400);
            resp.getWriter().println("Bad format");
        }
    }

    public void init(FilterConfig config) {
        //there could be init code, but ...
    }

    private boolean checkParentheses(String str) {
        Stack<Character> stack = new Stack<>();
        for (char c :
                str.toCharArray()) {
            if(c == ')') {
                if(stack.isEmpty()) {
                    return false;
                } else {
                    stack.pop();
                }
            } else if(c == '(') {
                stack.push(c);
            }
        }
        System.out.println("[+] Checked parentheses.");
        return stack.isEmpty();
    }

    private boolean checkExpr(String expr) {
        Pattern isExpr = Pattern.compile("^\\(*[A-Za-z0-9]([+\\-*/]\\(*[A-za-z0-9]\\)*)*\\)*$");
        StringTokenizer tokenizer = new StringTokenizer(expr, "() ");
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            builder.append(tokenizer.nextToken());
        }
        String cleanExpression = builder.toString();
        System.out.println("[+] Checked expression.");
        return isExpr.matcher(cleanExpression).matches();
    }

}
