package solution;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

@WebServlet(
        name = "123",
        urlPatterns = {"/calc/result"}
)
public class Result extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        String result = "";
        String equation = (String) session.getAttribute("equation");
        try {
            result = get( rPN(equation), req);
        } catch (IllegalAccessException e) {
            resp.setStatus(409);
        }
        if (resp.getStatus() != 409) {
            resp.setStatus(200);
            writer.write(result);
        } else {
            writer.write("bad format");
        }
    }

    private String get(String RPN, HttpServletRequest req) throws IllegalAccessException {
        StringTokenizer st = new StringTokenizer(RPN, ".");
        ArrayDeque<String> deque = new ArrayDeque<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumber(token)) {
                deque.push(token);
                continue;
            }
            if (isVar(token)) {
                String valueOfVar;
                valueOfVar = getVal(token, req);
                deque.push(valueOfVar);
                continue;
            }
            if (isOperator(token)) {
                String rhs = deque.pop();
                String lhs = deque.pop();
                deque.push(calc(Integer.parseInt(lhs), Integer.parseInt(rhs), token));
            }
        }
        return deque.getFirst();
    }

    private String rPN(String equation) {
        equation = equation.replaceAll("\\s", "");
        StringBuilder rpn = new StringBuilder();
        ArrayDeque<String> opStack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(equation, "+-*/()", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isNumber(token) || isVar(token)) {
                rpn.append(token);
                rpn.append('.');
            }
            if ("(".equals(token)) {
                opStack.push(token);
            }
            if (")".equals(token)) {
                while (!opStack.peek().equals( "(") ) {
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
        return rpn.toString();
    }

    private String calc(int lhs, int rhs, String op) {
        char c = op.charAt(0);
        switch (c) {
            case '+':
                return String.valueOf(lhs + rhs);
            case '-':
                return String.valueOf(lhs - rhs);
            case '*':
                return String.valueOf(lhs * rhs);
            case '/':
                return String.valueOf(lhs / rhs);
            default:
                return "";
        }
    }

    private boolean isVar(String str) {
        if (str.length() != 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z'))
                return false;
        }
        return true;
    }

    private boolean isNumber(String str) {
        if (str.charAt(0) == '-' && str.length() == 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-')
                continue;
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String getVal( String a, HttpServletRequest req) throws IllegalAccessException {
        HttpSession session = req.getSession(false);
        String s = (String) session.getAttribute(a);
        if (s == null) {
            throw new IllegalAccessException();
        }
        while (!isNumber(s)) {
            s = (String) session.getAttribute(s);
            if (s == null) {
                throw new IllegalAccessException();
            }
        }
        return s;
    }

    private boolean isOperator(String op) {
        return op.charAt(0) == '+' || op.charAt(0) == '-' || op.charAt(0) == '/' || op.charAt(0) == '*';
    }

    private int priority(String op) {
        if (op.charAt(0) == '*' || op.charAt(0) == '/')
            return 5;
        else if (op.charAt(0) == '+' || op.charAt(0) == '-')
            return 3;
        else
            return 0;
    }
}
