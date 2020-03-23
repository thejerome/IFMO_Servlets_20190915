package calculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "FinalServlet",
        urlPatterns = {"/calc/result"}
)
public class FinalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("equation") == null) {
            resp.setStatus(409);
            resp.getOutputStream().write("equation error".getBytes());
        } else if (httpSession.getAttribute("var") == null && containsLetters((String) httpSession.getAttribute("equation"))) {
            resp.setStatus(409);
            resp.getOutputStream().write("var".getBytes());
        } else {
            String equation = (String) httpSession.getAttribute("equation");
            Map<String, Object> varsMap = (HashMap<String, Object>) httpSession.getAttribute("var");
            try {
                int result = start(equation, varsMap);
                resp.getWriter().write(Integer.toString(result));
            } catch (InvalidParameterException e) {
                resp.setStatus(409);
                resp.getOutputStream().write("var2".getBytes());
            }
        }
    }

    private boolean containsLetters(String str) {
        for (char c : str.toCharArray()) {
            if (c >= 'A' && c <= 'Z')
                return true;
        }
        return false;
    }

    private boolean containsNiceLetters(String str) {
        for (char c : str.toCharArray()) {
            if (c >= 'a' && c <= 'z')
                return true;
        }
        return false;
    }

    private enum Operator {
        ADD(1), SUBTRACT(1), MULTIPLY(2), DIVIDE(2);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private String[] token(Map<String, Object> varsMap, String expr) {
        check(varsMap, expr);
        String newexpr = replace(varsMap, expr);
        return postfix(list(newexpr));
    }

    private boolean checkInt(Object var) {
        try {
            int val = (int) var;
            System.out.println(val);
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    private String replace(Map<String, Object> varsMap, String expr) {
        String equation = expr;
        for (Map.Entry<String, Object> var : varsMap.entrySet()) {
            Object value = var.getValue();
            if (checkInt(value)) {
                equation = equation.replaceAll(var.getKey(), var.getValue().toString());
            } else {
                equation = equation.replaceAll(var.getKey(), (String) var.getValue());
            }

        }

        if (containsNiceLetters(equation)) {
            return replace(varsMap, equation);
        }
        return equation;
    }

    private boolean containOp(String str) {
        for (char c : str.toCharArray()) {
            if (c == '+' || c == '*' || c == '/' || c == '(' || c == ')')
                return true;
        }
        return false;
    }

    private boolean containOpWMinus(String str) {
        for (char c : str.toCharArray()) {
            if (c == '+' || c == '*' || c == '/' || c == '(' || c == ')' || c == '-')
                return true;
        }
        return false;
    }


    private LinkedList<String> list(String expression) {
        StringBuilder buf = new StringBuilder();
        LinkedList<String> tokens = new LinkedList<>();

        String expr = expression.replaceAll("\\s+", "");
        String[] parts = expr.split("");

        for (int i = 0; i < parts.length; i++) {
            if (i == 0 && "-".equals(parts[i])) {
                buf.append("-");
            } else if ("-".equals(parts[i]) && containOp(parts[i - 1])) {
                buf.append("-");
            } else if (Pattern.matches("[0-9]", parts[i])) {
                buf.append(parts[i]);
            } else if (containOpWMinus(parts[i])) {
                if (buf.length() > 0) {
                    tokens.add(buf.toString());
                    buf.delete(0, buf.length());
                }
                tokens.add(parts[i]);
            } else if (containsNiceLetters(parts[i])) {
                tokens.add(parts[i]);
            }
            if (i == parts.length - 1 && buf.length() > 0) {
                tokens.add(buf.toString());
                buf.delete(0, buf.length());
            }
        }

        return tokens;
    }

    private String[] postfix(List<String> tokens) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : tokens) {
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && prec(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);
            } else if ("(".equals(token)) {
                stack.push(token);
            } else if (")".equals(token)) {
                while (!"(".equals(stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.pop();
            } else {
                output.append(token).append(' ');
            }
        }


        while (!stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString().split(" ");
    }

    public int start(String expr, Map<String, Object> vars) {
        return answer(token(vars, expr));
    }

    private boolean prec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    private int answer(String[] tokens) {
        Deque<String> solutionstack = new ArrayDeque<>();
        for (String el : tokens) {
            if (Pattern.matches("-?[0-9]{1,10}", el)) {
                solutionstack.push(el);
            } else if (ops.containsKey(el)) {
                int number1 = Integer.parseInt(solutionstack.pop());
                int number2 = Integer.parseInt(solutionstack.pop());
                int result = 0;
                switch (el) {
                    case "+":
                        result = number2 + number1;
                        break;
                    case "-":
                        result = number2 - number1;
                        break;
                    case "*":
                        result = number2 * number1;
                        break;
                    case "/":
                        result = number2 / number1;
                        break;
                    default:
                        break;
                }
                solutionstack.push(String.valueOf(result));
            }
        }
        return Integer.parseInt(solutionstack.pop());
    }

    private void check(Map<String, Object> varsMap, String expr) {
        List<Character> chars = new LinkedList<>();
        for (char c : expr.toCharArray()) {
            if (c >= 'a' && c <= 'z')
                chars.add(c);
        }
        for (char var : chars) {
            if (varsMap.get(String.valueOf(var)) == null) {
                throw new InvalidParameterException("Incorrect vars map");
            }
        }
    }
}
