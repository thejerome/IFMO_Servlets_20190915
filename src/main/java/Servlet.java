import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.util.*;

@WebServlet(
        name = "Servlet",
        urlPatterns = {"/calc"}
)
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getParameter("equation");
        equation = equation.replaceAll("\\s+","");
        StringBuilder rpn = new StringBuilder();
        ArrayDeque<String> opStack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(equation, "+-*/()", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumberOrVar(token)) {
                rpn.append(token);
                rpn.append('.');
            }
            if (token.equals("(")) {
                opStack.push(token);
            }
            if (token.equals(")")) {
                while (!opStack.peek().equals("(")) {
                    String op = opStack.pop();
                    rpn.append(op);
                    rpn.append('.');
                }
                opStack.pop();
            }
            if (isOperator(token)) {
                if (opStack.peek() != null) {
                    while (priority(opStack.peek()) >= priority(token)) {
                        String op = opStack.pop();
                        rpn.append(op);
                        rpn.append('.');
                        if (opStack.peek() == null)
                            break;
                    }
                }
                opStack.push(token);
            }
        }
        while (opStack.peek() != null) {
            String op = opStack.pop();
            rpn.append(op);
            rpn.append('.');
        }
        rpn.setLength(rpn.length() - 1);
        System.out.println(rpn.toString());
        st = new StringTokenizer(rpn.toString(), ".");
        ArrayDeque<String> calc = new ArrayDeque<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumber(token)) {
                calc.push(token);
                continue;
            }
            if (isVar(token)){
                String valueOfVar = getValueOfVar(req, token);
                calc.push(valueOfVar);
                continue;
            }
            if (isOperator(token)){
                if (calc.size() < 2) throw new AssertionError();
                String rhs = calc.pop();
                String lhs = calc.pop();
                calc.push(calculate(Integer.parseInt(lhs), Integer.parseInt(rhs), token));
            }
        }
        PrintWriter writer = resp.getWriter();
        writer.write(calc.getFirst());
        writer.flush();
        writer.close();
    }

    String calculate(int lhs, int rhs, String op){
        char c = op.charAt(0);
        switch (c){
            case '+':
                return String.valueOf(lhs+rhs);
            case '-':
                return String.valueOf(lhs-rhs);
            case '*':
                return String.valueOf(lhs*rhs);
            case '/':
                return String.valueOf(lhs/rhs);
        }
        return "";
    }

    boolean isNumberOrVar(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z'))
                    return false;
            }
        }
        return true;
    }

    boolean isVar(String str) {
        if (str.length() != 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z'))
                return false;
        }
        return true;
    }

    boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    String getValueOfVar(HttpServletRequest req, String a) {
        String s = req.getParameter(a);
        while (!isNumber(s)) {
            s = req.getParameter(s);
        }
        return s;
    }

    boolean isOperator(String op) {
        char c = op.charAt(0);
        return c == '+' || c == '-' || c == '/' || c == '*';
    }

    int priority(String op) {
        char c = op.charAt(0);
        if (c == '*' || c == '/')
            return 2;
        else if (c == '+' || c == '-')
            return 1;
        else
            return 0;
    }
}

/*
    if the token is a left paren (i.e. "("), then:
        push it onto the operator stack.
    if the token is a right paren (i.e. ")"), then:
        while the operator at the top of the operator stack is not a left paren:
            pop the operator from the operator stack onto the output queue.

        if there is a left paren at the top of the operator stack, then:
                pop the operator from the operator stack and discard it
                after while loop, if operator stack not null, pop everything to output queue
                if there are no more tokens to read then:
                while there are still operator tokens on the stack:
                pop the operator from the operator stack onto the output queue.
                exit.
*/
