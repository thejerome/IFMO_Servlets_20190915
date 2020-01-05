package com.ifmo.aumetov;

import java.util.*;

public class CalcMethod {
    private static String operators = "+-*/";
    private static String delimiters = "()" + operators;

    private static boolean isDelimiters (String variable) {
        int i = 0;

        while (i < delimiters.length()) {
            if (variable.charAt(0) == delimiters.charAt(i)) {
                return true;
            }
            i++;
        }
        return false;
    }


    private static int priority (String variable) {
        String openBracket = "(";
        String closeBracket = ")";
        String addition = "+";
        String subtraction = "-";
        String multiplication = "*";
        String division = "/";

        if (openBracket.equals(variable) || closeBracket.equals(variable)) {
            return 1;
        }
        if (addition.equals(variable) || subtraction.equals(variable)) {
            return 2;
        }
        if (multiplication.equals(variable) || division.equals(variable)) {
            return 3;
        }
        return 4;
    }

    public static List<String> parse (String infixNotation) {
        List<String> postfix = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        StringTokenizer tokenizer = new StringTokenizer(infixNotation,
                                                        delimiters,
                                                        true);

        while (tokenizer.hasMoreTokens()) {
            String current = "";
            current = tokenizer.nextToken();
            String openBracket = "(";
            String closeBracket = ")";

            if (isDelimiters(current) && openBracket.equals(current)) {
                stack.push(current);
            } else if (isDelimiters(current) && closeBracket.equals(current)) {
                while (!stack.peek().equals(openBracket)) {
                    postfix.add(stack.pop());
                }
                stack.pop();

            } else if (isDelimiters(current) && closeBracket.equals(current) && !stack.isEmpty()) {
                postfix.add(stack.pop());
            } else if (isDelimiters(current)) {
                while (!stack.isEmpty() && (priority(stack.peek()) >= priority(current))) {
                    postfix.add(stack.pop());
                }
                stack.push(current);
            } else {
                postfix.add(current);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }
}
