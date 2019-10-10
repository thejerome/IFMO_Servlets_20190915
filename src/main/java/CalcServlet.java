import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getParameterMap().get("equation")[0];
        Map<String, Object> expMap = new HashMap<>();
        for (Map.Entry<String, String[]> param : req.getParameterMap().entrySet()) {
            if (!param.getKey().equals("equation")) {
                expMap.put(param.getKey(), param.getValue()[0]);
            }
        }

        int equationResult = parse_tokenize(equation, expMap);
        resp.getWriter().println(equationResult);

    }

    private static Map<String, Integer> ops = new HashMap<String, Integer>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
    }};

    private static boolean isHigerPrec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub) >= ops.get(op));
    }

    private int  parse_tokenize(String equation, Map<String, Object> expMap){
        String replacedExp = replace(expMap, equation);
        return solve(postfix(tokensList(replacedExp)));
    }

    private static String replace(Map<String, Object> varsMap, String expr) {

        for (Map.Entry<String, Object> entry : varsMap.entrySet()) {
            expr = expr.replaceAll(entry.getKey(), entry.getValue().toString());
        }

        Matcher matcher = Pattern.compile("[a-z]").matcher(expr);
        if (matcher.find()) {
            return replace(varsMap, expr);
        }
        return expr;
    }

    private static ArrayList<String> tokensList(String expression) {
        String expr = expression.replaceAll("\\s+", "");
        String[] lit = expr.split("");
        StringBuilder numbers = new StringBuilder();
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < lit.length; i++) {
            if (i == 0 && lit[i].equals("-")) {
                numbers.append("-");
            } else if (lit[i].equals("-") && Pattern.matches("[+*/()]", lit[i - 1])) {
                numbers.append("-");
            } else if (Pattern.matches("[0-9]", lit[i])) {
                numbers.append(lit[i]);
            } else if (Pattern.matches("[-+*/()]", lit[i])) {
                if (numbers.length() > 0) {
                    tokens.add(numbers.toString());
                    numbers.delete(0, numbers.length());
                }
                tokens.add(lit[i]);
            } else if (Pattern.matches("[a-z]", lit[i])) {
                tokens.add(lit[i]);
            }
            if (i == lit.length - 1 && numbers.length() > 0) {
                tokens.add(numbers.toString());
                numbers.delete(0, numbers.length());
            }
        }

        return tokens;
    }

    private static String[] postfix(ArrayList<String> tokens) {
        StringBuilder output = new StringBuilder();
        Deque<String> tokenStack = new LinkedList<>();

        for (String token : tokens) {
            if (ops.containsKey(token)) {
                while (!tokenStack.isEmpty() && isHigerPrec(token, tokenStack.peek()))
                    output.append(tokenStack.pop()).append(' ');
                tokenStack.push(token);
            } else if ("(".equals(token)) {
                tokenStack.push(token);
            } else if (")".equals(token)) {
                while (!"(".equals(tokenStack.peek()))
                    output.append(tokenStack.pop()).append(' ');
                tokenStack.pop();
            } else {
                output.append(token).append(' ');
            }
        }

        while (!tokenStack.isEmpty())
            output.append(tokenStack.pop()).append(' ');

        return output.toString().split(" ");
    }

    private static int solve(String[] tokens) {
        Deque<String> tokenStack = new LinkedList<>();
        for (String token : tokens) {
            if (Pattern.matches("-?[0-9]{1,10}", token)) {
                tokenStack.push(token);
            } else if (ops.containsKey(token)) {
                int number1 = Integer.parseInt(tokenStack.pop());
                int number2 = Integer.parseInt(tokenStack.pop());
                int result = 0;
                switch (token) {
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
                }
                tokenStack.push(String.valueOf(result));
            }
        }
        return Integer.parseInt(tokenStack.pop());
    }
}
