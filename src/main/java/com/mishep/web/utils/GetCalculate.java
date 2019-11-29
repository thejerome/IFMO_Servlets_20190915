package com.mishep.web.utils;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.StringTokenizer;

public class GetCalculate {
    public static String getReversePolishNotation(String equation) {
        StringBuilder rpn = new StringBuilder();
        ArrayDeque<String> signStack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(equation, "+-*/()", true);
        do {
            String token = st.nextToken();
            int i = 0;
            boolean numberOrVarValue = true;
            do {
                if (!Character.isDigit(token.charAt(i)) && !(token.charAt(i) >= 'a' && token.charAt(i) <= 'z')) {
                    numberOrVarValue = false;
                }
                i++;
            } while (i < token.length());

            if ("(".equals(token)) {
                signStack.push(token);
            } else if (")".equals(token)) {
                do {
                    String op = signStack.pop();
                    rpn.append(op);
                    rpn.append('.');
                } while (!Objects.equals(signStack.peek(), "("));
                signStack.pop();
            } else if (numberOrVarValue) {
                rpn.append(token);
                rpn.append('.');
            } else if (isOperator(token)) {
                if (signStack.peek() != null) {
                    while (priorityOfOperator(signStack.peek()) >= priorityOfOperator(token)) {
                        String op = signStack.pop();
                        rpn.append(op);
                        rpn.append('.');
                        if (signStack.peek() == null)
                            break;
                    }
                }
                signStack.push(token);
            }
        } while (st.hasMoreTokens());
        do {
            String op = signStack.pop();
            rpn.append(op);
            rpn.append('.');
        } while (signStack.peek() != null);
        rpn.setLength(rpn.length() - 1);
        return rpn.toString();
    }

    public static int calcSimpleEquation(int op1, int op2, String op) {
        char c = op.charAt(0);
        switch (c) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                return op1 / op2;
            default:
                return 0;
        }
    }

    public static boolean isOperator(String op) {
        char c = op.charAt(0);
        return c == '+' || c == '-' || c == '/' || c == '*';
    }

    private static int priorityOfOperator(String op) {
        char c = op.charAt(0);
        byte tmp;
        switch (c) {
            case '*':
            case '/':
                tmp = 2;
                break;
            case '+':
            case '-':
                tmp = 1;
                break;
            default:
                tmp = 0;
        }
        return tmp;
    }
}
