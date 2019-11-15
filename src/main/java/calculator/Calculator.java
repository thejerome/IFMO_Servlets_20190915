package calculator;

import java.util.*;
import java.lang.*;

public class Calculator {
    private static String operators = "+-*/";
    private static String delimiters = "() " + operators;
    private boolean flag = true;


    public static int compute(String equation) {
        Calculator n = new Calculator();
        List<String> expression = n.parse(equation);
        boolean flag = n.flag;
        if (flag) {
            return calc(expression);
        }
        return 0;
    }


    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isOperator(String token) {
        if (token.equals("u-")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (token.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isFunction(String token) {
        return token.equals("sqrt") || token.equals("cube") || token.equals("pow10");
    }

    private static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        return 4;
    }

    private List<String> parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr;
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                flag = false;
                return postfix;
            }
            if (curr.equals(" ")) continue;
            if (isFunction(curr)) stack.push(curr);
            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!"(".equals(stack.peek())) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            flag = false;
                            return postfix;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                } else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev) && !prev.equals(")")))) {
                        curr = "u-";
                    } else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                    }
                    stack.push(curr);
                }

            } else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else {
                flag = false;
                return postfix;
            }
        }
        return postfix;
    }


        private static int calc(List<String> postfix) {
            Deque<Integer> stack = new ArrayDeque<>();
            for (String x : postfix) {
                switch (x) {
                    case "+":
                        stack.push(stack.pop() + stack.pop());
                        break;
                    case "-": {
                        int b = stack.pop(), a = stack.pop();
                        stack.push(a - b);
                        break;
                    }
                    case "*":
                        stack.push(stack.pop() * stack.pop());
                        break;
                    case "/": {
                        int b = stack.pop(), a = stack.pop();
                        stack.push(a / b);
                        break;
                    }
                    case "u-":
                        stack.push(-stack.pop());
                        break;
                    default:
                        stack.push(Integer.parseInt(x));
                        break;
                }
            }
            return stack.pop();


    }

}