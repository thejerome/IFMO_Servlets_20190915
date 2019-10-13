package com.KyPtKa.OG;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator{

    private String equation;
    private Map<String, String> vars;
    private boolean computable;

    Calculator(String equation, Map<String, String[]> params) {
        this.equation = equation;
        this.vars = extractVars(params);
        this.computable = vars.values().stream()
                .anyMatch(var -> !isNumeric(var));
    }

    private Map<String, String> extractVars(Map<String, String[]> params) {
        Map<String, String> vars = new HashMap<>();
        for (String key : params.keySet()) {
            vars.put(key, params.get(key)[0]);
        }
        return vars;
    }

    public String solve() {
        equation = equation.replace(" ", "").replace("", " ").trim();
        for (int i = 0; i < vars.size(); i++) {
            for (String var : vars.keySet()) {
                equation = equation.replace(var, vars.get(var));
            }
        }

        if (!computable) {
            return equation;
        }

        return String.valueOf(evaluate(equation));
    }

    private static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sbuf.append(tokens[i++]);
                }
                values.push(Integer.parseInt(sbuf.toString()));
            } else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            default:
                return 0;
        }

    }

    private boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
