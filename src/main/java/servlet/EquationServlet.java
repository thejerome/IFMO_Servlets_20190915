package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
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
        List<String> polishTokens = null;
        try {
            polishTokens = parseIntoPolishNotation(equation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = calculate(polishTokens, args);
        resp.getOutputStream().print(result);
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private List<String> parseIntoPolishNotation(String equation) throws ParseException {
        if (!(equation.contains("+")
                || equation.contains("-")
                || equation.contains("*")
                || equation.contains("/"))) {
            throw new ParseException("notValidEquation", 0);
        }
        StringTokenizer tokenizer = new StringTokenizer(equation.replaceAll(" ", ""), "()+-/*", true);

        Deque<String> stack = new ArrayDeque<>();
        List<String> polishNotation = new ArrayList<>();

        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                switch (token) {
                    case "+":
                    case "-":
                    case ")":
                        iterateAndRemove("+-/*", stack, polishNotation);
                        if (")".equals(token)) {
                            if (!stack.getFirst().equals("("))
                                throw new ParseException("notValidEquation", 0);
                            stack.removeFirst();
                        } else {
                            stack.addFirst(token);
                        }
                        break;
                    case "*":
                    case "/":
                        iterateAndRemove("/*", stack, polishNotation);
                        stack.addFirst(token);
                        break;
                    case "(":
                        stack.addFirst(token);
                        break;
                    default:
                        polishNotation.add(token);
                }
            } catch (NoSuchElementException e) {
                if (!")".equals(token)) {
                    stack.addFirst(token);
                }
            }
        }
        while (!stack.isEmpty()) {
            if (stack.getFirst().equals("("))
                throw new ParseException("notValidEquation", 0);
            polishNotation.add(stack.removeFirst());
        }
        return polishNotation;
    }

    private void iterateAndRemove(String match, Deque<String> stack, List<String> polishNotation) {
        while (match.contains(stack.getFirst())) {
            polishNotation.add(stack.removeFirst());
        }
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