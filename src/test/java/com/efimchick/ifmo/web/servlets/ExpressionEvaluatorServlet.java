package com.efimchick.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/calc")
public class ExpressionEvaluatorServlet extends HttpServlet {

    private static final String EXPRESSION_PARAM_NAME = "equation";

    private static final Map<String, BiFunction<Integer, Integer, Integer>> OPERATION_MAP = new HashMap<String, BiFunction<Integer, Integer, Integer>>() {{
        put("+", Integer::sum);
        put("-", (a, b) -> a - b);
        put("*", (a, b) -> a * b);
        put("/", (a, b) -> {
            if (b == 0) {
                throw new IllegalArgumentException("Divide by zero is not allowed!");
            }
            return a / b;
        });
    }};


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String expression = req.getParameter(EXPRESSION_PARAM_NAME);
        String preparedExpression = prepareExpression(expression, req.getParameterMap());
        int result = evaluate(preparedExpression);
        resp.getOutputStream().print(preparedExpression + " = " + result);
    }

    private String prepareExpression(String expression, Map<String, String[]> params) {
        String preparedString = expression;
        List<String> keys =
                params.keySet().stream()
                        .filter(key -> !EXPRESSION_PARAM_NAME.equalsIgnoreCase(key))
                        .sorted(Comparator.comparingInt(String::length).reversed())
                        .collect(Collectors.toList());

        for (String key : keys) {
            preparedString = preparedString.replaceAll(key, params.get(key)[0]);
        }
        return preparedString;
    }

    private int evaluate(String expression) {
        char[] arr = expression.toCharArray();

        Stack<Integer> values = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ' ') {
                continue;
            }

            if (arr[i] >= '0' && arr[i] <= '9') {
                StringBuilder builder = new StringBuilder();
                while (i < arr.length && Character.isDigit(arr[i])) {
                    builder.append(arr[i++]);
                }
                i--;
                values.push(Integer.valueOf(builder.toString()));
            } else if (arr[i] == '(') {
                operations.push(arr[i]);
            } else if (arr[i] == ')') {
                while (operations.peek() != '(') {
                    values.push(performOperation(operations.pop(), values.pop(), values.pop()));
                }
                operations.pop();
            } else if (arr[i] == '+' || arr[i] == '-' || arr[i] == '*' || arr[i] == '/') {
                while (!operations.empty() && hasPrecedence(arr[i], operations.peek())) {
                    values.push(performOperation(operations.pop(), values.pop(), values.pop()));
                }
                operations.push(arr[i]);
            }
        }

        while (!operations.empty()) {
            values.push(performOperation(operations.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private int performOperation(char operation, int b, int a) {
        return OPERATION_MAP.getOrDefault(String.valueOf(operation), (p, q) -> 0).apply(a, b);
    }

}
