package com.weblab.rbetik12.utils;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CalcUtil {

    public static int parse(String expr, Map<String, Object> vars) {
        return solve(tokenize(expr, vars));
    }

    private static String[] tokenize(String expr, Map<String, Object> varsMap) {
        checkVarsMap(varsMap, expr);
        String replacedExpr = replaceVars(varsMap, expr);
        return buildPostfix(buildTokensList(replacedExpr));
    }

    private static String replaceVars(Map<String, Object> varsMap, String expr) {
        for (Map.Entry<String, Object> var : varsMap.entrySet()) {
            Object value = var.getValue();
            if (value instanceof Integer) {
                expr = expr.replaceAll(var.getKey(), var.getValue().toString());
            } else {
                expr = expr.replaceAll(var.getKey(), (String) var.getValue());
            }

        }
        Matcher matcher = Pattern.compile("[a-z]").matcher(expr);
        if (matcher.find()) {
            return replaceVars(varsMap, expr);
        }
        return expr;
    }

    private static ArrayList<String> buildTokensList(String expression) {
        String expr = expression.replaceAll("\\s+", "");
        String[] literals = expr.split("");
        StringBuilder numberBuffer = new StringBuilder();
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < literals.length; i++) {
            if (i == 0 && literals[i].equals("-")) {
                numberBuffer.append("-");
            } else if (literals[i].equals("-") && Pattern.matches("[+*/()]", literals[i - 1])) {
                numberBuffer.append("-");
            } else if (Pattern.matches("[0-9]", literals[i])) {
                numberBuffer.append(literals[i]);
            } else if (Pattern.matches("[-+*/()]", literals[i])) {
                if (numberBuffer.length() > 0) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.delete(0, numberBuffer.length());
                }
                tokens.add(literals[i]);
            } else if (Pattern.matches("[a-z]", literals[i])) {
                tokens.add(literals[i]);
            }
            if (i == literals.length - 1 && numberBuffer.length() > 0) {
                tokens.add(numberBuffer.toString());
                numberBuffer.delete(0, numberBuffer.length());
            }
        }

        return tokens;
    }

    private static String[] buildPostfix(ArrayList<String> tokens) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : tokens) {
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrec(token, stack.peek()))
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

    private enum Operator {
        ADD(1), SUBTRACT(1), MULTIPLY(2), DIVIDE(2);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean isHigerPrec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    private static int solve(String[] tokens) {
        Deque<String> stack = new LinkedList<>();
        for (String token : tokens) {
            if (Pattern.matches("-?[0-9]{1,10}", token)) {
                stack.push(token);
            } else if (ops.containsKey(token)) {
                int number1 = Integer.parseInt(stack.pop());
                int number2 = Integer.parseInt(stack.pop());
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
                    default:
                        break;
                }
                stack.push(String.valueOf(result));
            }
        }
        return Integer.parseInt(stack.pop());
    }

    private static void checkVarsMap(Map<String, Object> varsMap, String expr) {
        List<Character> vars = expr.chars()
                .mapToObj(i -> (char) i)
                .filter(Character::isAlphabetic)
                .collect(Collectors.toList());
        for (char var : vars) {
            if (varsMap.get(String.valueOf(var)) == null) {
                throw new InvalidParameterException("Incorrect vars map");
            }
        }
    }
}
