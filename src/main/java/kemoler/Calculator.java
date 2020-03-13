package kemoler;

import java.util.Map;
import java.util.Stack;

public class Calculator {

    private static int getPriority(char c) {
        if (c == '+' || c == '-') return 1;
        if (c == '*' || c == '/') return 2;
        return 0;
    }

    private static boolean isDigit(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    private static boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static String makePostfix(String infixEquation) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfixEquation = new StringBuilder(infixEquation.length());

        for (char c: infixEquation.toCharArray()) {
            if (isLetter(c)) postfixEquation.append(c);
            else if (c == '(') stack.push(c);
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfixEquation.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() != '(') return null;
                else stack.pop();
            } else if (isOperator(c)) {
                if (!stack.isEmpty() && getPriority(c) <= getPriority(stack.peek())) postfixEquation.append(stack.pop());
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            postfixEquation.append(stack.pop());
        }
        return postfixEquation.toString();
    }

    public static String eval(String equation, Map<String, String> map) {
        Stack<Integer> stack = new Stack<>();
        for (char c : equation.toCharArray()) {
            if (isLetter(c)) {
                stack.push(Integer.parseInt(resolveVar(c, map)));
            } else {
                int rightOperand = stack.pop();
                int leftOperand = stack.pop();
                stack.push(calculateTwo(leftOperand, rightOperand, Character.toString(c)));
            }
        }
        return String.valueOf(stack.pop());
    }

    private static Integer calculateTwo(int leftOperand, int rightOperand, String operator) {
        if ("+".equals(operator)) return leftOperand + rightOperand;
        if ("-".equals(operator)) return leftOperand - rightOperand;
        if ("*".equals(operator)) return leftOperand * rightOperand;
        if ("/".equals(operator)) return leftOperand / rightOperand;
        return null;
    }

    private static String resolveVar(char k, Map<String, String> map) {
        String v;
        if (!Character.isAlphabetic(k)) {
            return map.get(Character.toString(k));
        } else {
            v = map.get(Character.toString(k));
            if (isDigit(v)) return v;
            else return resolveVar(v.charAt(0), map);
        }
    }
}
