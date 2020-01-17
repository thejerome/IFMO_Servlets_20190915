import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "Equation", urlPatterns = {"/calc/equation"})

public class Equation extends HttpServlet {
    private static boolean isOperator(char character) {
        return character == '+' || character == '-' || character == '*' || character == '/';
    }
    private static boolean isDelimiter(char character) {
        return character == '(' || character == ')';
    }
    static boolean isVariable(char character) {
        return character >= 'a' && character <= 'z';
    }
    private static boolean isNumber(char character) {
        return character >= '0' && character <= '9';
    }
    private static boolean correctExpression(String exp) {
        for (int i = 0; i < exp.length(); i++) {
            if (!(isNumber(exp.charAt(i)) || isOperator(exp.charAt(i)) || isVariable(exp.charAt(i)) || isDelimiter(exp.charAt(i)) || (exp.charAt(i) == ' ')) || (i != 0 && isVariable(exp.charAt(i)) && isVariable(exp.charAt(i-1)))) {
                return false;
            }
        }
        return true;
    }
    @Override
    protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession();
        PrintWriter writer = httpServletResponse.getWriter();
        String eq = httpServletRequest.getReader().readLine();
        if (!correctExpression(eq)) {
            writer.println("expression isn't correct");
            httpServletResponse.setStatus(400);
        }
        else if (session.getAttribute("equation") == null) {
            session.setAttribute("equation", eq);
            httpServletResponse.setStatus(201);
        }
        else {
            httpServletResponse.setStatus(200);
            session.setAttribute("equation", eq);
        }
        writer.close();
    }

    @Override
    protected void doDelete (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletRequest.getSession().removeAttribute("equation");
        httpServletResponse.setStatus(204);
    }
}

