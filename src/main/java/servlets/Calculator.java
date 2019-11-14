package servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Stack;


@WebServlet(
        name = "Calculator",
        urlPatterns = {"/calc"}
)
public class Calculator extends HttpServlet {
    private Stack<String>  stack = new Stack<>();
    private Stack<String>  nstack = new Stack<>();
    private int prev = 0;

    private void stackaction() {
        stack.push(String.valueOf(nstack.peek()));
        nstack.pop();
    }

    private boolean equals(String s) {
        return nstack.peek().equals(s);
    }

    private void equationtostack(char[] modequation) {
        for (char c : modequation)
            if ('0' <= c && c <= '9') {
                int j;
                if (!stack.isEmpty() && prev == 1) {
                    if (stack.peek().matches("[-+]?\\d+")) {
                        j = Integer.parseInt(stack.peek().trim());
                        stack.pop();
                        stack.push(j + String.valueOf(c));
                        prev = 1;
                    }
                } else {
                    stack.push(String.valueOf(c));
                    prev = 1;
                }
            } else {
                if (c == '+' || c == '-') {
                    if ((!nstack.isEmpty()) && (equals("+") || equals("-") ||
                            equals("*") || equals("/"))) {
                        stackaction();
                    }
                    nstack.push(String.valueOf(c));
                    prev = 0;
                }
                if (c == '*' || c == '/') {
                    if ((!nstack.isEmpty()) && (equals("*") || equals("/"))) {
                        stackaction();
                    }
                    nstack.push(String.valueOf(c));
                    prev = 0;
                }
                if (c == '(') {
                    nstack.push("(");
                    prev = 0;
                }
                if (c == ')') {
                    prev = 0;
                    do {
                        stackaction();
                    } while (!equals("("));
                    nstack.pop();
                }
            }
        while (!stack.isEmpty()) {
            nstack.push(stack.peek());
            stack.pop();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        Map<String, String[]> allMap = req.getParameterMap();
        int a = 0;char[] modequation;
        while (a!=2) {
            modequation = equation.toCharArray();
            for (char c : modequation)
                if (Character.isLetter(c)) {
                    equation = equation.replaceAll(String.valueOf(c),
                            String.valueOf(allMap.get(String.valueOf(c))[0]));
                }
            a++;
        }
        modequation = equation.toCharArray();
        equationtostack(modequation);
        int leftoperand = 0;
        while (!nstack.isEmpty()) {
            leftoperand = Integer.parseInt(nstack.peek().trim());
            stack.push(nstack.peek());
            nstack.pop();
            int rightoperand;
            if (!nstack.isEmpty())
                rightoperand = Integer.parseInt(nstack.peek().trim());
            else break;
            stack.push(nstack.peek());
            nstack.pop();
            while (nstack.peek().matches("[-+]?\\d+")) {
                leftoperand = rightoperand;
                rightoperand = Integer.parseInt(nstack.peek().trim());
                stack.push(nstack.peek());
                nstack.pop();
            }
            switch (nstack.peek()) {
                case ("+"):
                    leftoperand += rightoperand;
                    break;
                case ("-"):
                    leftoperand -= rightoperand;
                    break;
                case ("*"):
                    leftoperand *= rightoperand;
                    break;
                case ("/"):
                    leftoperand = leftoperand / rightoperand;
                    break;
                default :
                    break;
            }
            nstack.pop();
            nstack.push(String.valueOf(leftoperand));
            stack.pop();
            stack.pop();
            while (!stack.isEmpty()) {
                nstack.push(stack.peek());
                stack.pop();
            }
        }
        out.print(leftoperand);
        nstack.clear();
        stack.clear();
        out.flush();
        out.close();
    }
}