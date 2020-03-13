package kemoler;

import java.util.Map;
import java.util.Stack;

public class Calculator {

    private static int getPriority(char c) {
        if (c == '+' || c == '-') return 1;
        if (c == '*' || c == '/') return 2;
        return 0;
    }

    public static boolean isDigit(String str) {
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
            if (isLetter(c) || isDigit(Character.toString(c))) postfixEquation.append(c);
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
        System.out.println(postfixEquation.toString());
        return postfixEquation.toString();
    }

    public static String eval(String equation, Map<String, String> map) throws Exception {
        Stack<Integer> stack = new Stack<>();
        for (char c : equation.toCharArray()) {
            if (isLetter(c)) {
                stack.push(Integer.parseInt(resolveVar(c, map)));
            } else if (isDigit(Character.toString(c))) {
                stack.push(Integer.parseInt(Character.toString(c)));
            } else {
                int right = stack.pop();
                int left = stack.pop();
                stack.push(calculateTwo(left, right, Character.toString(c)));
            }
        }
        return String.valueOf(stack.pop());
    }

    private static Integer calculateTwo(int left, int right, String operator) {
        if ("+".equals(operator)) return left + right;
        if ("-".equals(operator)) return left - right;
        if ("*".equals(operator)) return left * right;
        if ("/".equals(operator)) return left / right;
        return null;
    }

    private static String resolveVar(char k, Map<String, String> map) throws Exception {
        String v;
        try {
            if (!Character.isAlphabetic(k)) {
                return map.get(Character.toString(k));
            } else {
                v = map.get(Character.toString(k));
                if (isDigit(v)) return v;
                else return resolveVar(v.charAt(0), map);
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
