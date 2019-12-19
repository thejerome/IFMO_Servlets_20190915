package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(
        urlPatterns = {"/calc"}
)
public class EquationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String equation = req.getParameter("equation");

        Map<String, Integer> args = new HashMap<>();
        Map<String, String> StringValue = new HashMap<>();
        for (Map.Entry<String, String[]> par : req.getParameterMap().entrySet()) {
            if (par.getKey().equals("equation")) continue;
            try {
                args.put(par.getKey(), Integer.parseInt(par.getValue()[0]));
            } catch (NumberFormatException e) {
                StringValue.put(par.getKey(), par.getValue()[0]);
            }
        }
        for (String key : StringValue.keySet()) {
            Integer value = args.get(StringValue.get(key));
            args.put(key, value);
        }
        List<String> polishTokens = parseIntoPolishNotation(equation);
        int result = calculate(polishTokens, args);
        resp.getOutputStream().print(result);
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private List<String> parseIntoPolishNotation(String equation) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(equation.replaceAll(" ", ""), "()+-/*", true);
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }
        Deque<String> stack = new ArrayDeque<>();
        List<String> polishNotation = new ArrayList<>();
        for (String token : tokens) {
            try {
                switch (token) {
                    case "+":
                    case "-":
                    case ")":
                        while ("+-/*".contains(stack.getFirst())) {
                            polishNotation.add(stack.removeFirst());
                        }
                        if (")".equals(token)) {
                            stack.removeFirst();
                        } else {
                            stack.addFirst(token);
                        }
                        break;
                    case "*":
                    case "/":
                        while ("/*".contains(stack.getFirst())) {
                            polishNotation.add(stack.removeFirst());
                        }
                        stack.addFirst(token);
                        break;
                    case "(":
                        stack.addFirst(token);
                        break;
                    default:
                        polishNotation.add(token);
                }
            } catch (NoSuchElementException e) {
                if (!(")").equals(token))
                    stack.addFirst(token);
            }
        }
        while (!stack.isEmpty()) {
            polishNotation.add(stack.removeFirst());
        }
        return polishNotation;
    }

    private static int calculate(List<String> polishNotation, Map<String, Integer> args) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : polishNotation) {
            int first;
            int second;
            switch (token) {
                case "+":
                    stack.addFirst(stack.removeFirst() + stack.removeFirst());
                    break;
                case "-":
                    first = stack.removeFirst();
                    second = stack.removeFirst();
                    stack.addFirst(second - first);
                    break;
                case "*":
                    stack.addFirst(stack.removeFirst() * stack.removeFirst());
                    break;
                case "/":
                    first = stack.removeFirst();
                    second = stack.removeFirst();
                    stack.addFirst(second / first);
                    break;
                default:
                    if(args.containsKey(token)) {
                        stack.addFirst(args.get(token));
                    }
                    break;

            }
        }
        return stack.removeFirst();
    }
}