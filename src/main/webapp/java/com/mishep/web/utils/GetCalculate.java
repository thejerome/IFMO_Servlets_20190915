package com.mishep.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.StringTokenizer;

public class GetCalculate {
    public static String getResult(HttpServletRequest req, String tokenStr) {
        StringTokenizer str = new StringTokenizer(tokenStr, ".");
        ArrayDeque<String> calc = new ArrayDeque<>();
        do {
            String tokenizer = str.nextToken();
            if (numberValue(tokenizer)) {
                calc.push(tokenizer);
                continue;
            }
            if (varValue(tokenizer)) {
                String a = tokenizer;
                while (a.charAt(0) >= 'a' && a.charAt(0) <= 'z'){
                    a = (String) (req.getSession()).getAttribute(a);
                    if (a == null)
                        throw new IllegalArgumentException();
                }
                if (tokenizer.charAt(0) >= 'a' && tokenizer.charAt(0) <= 'z'){
                    calc.push(a);
                } else {
                    calc.push(tokenizer);
                }
                continue;
            }
            if (GetCalculate.isOperator(tokenizer)) {
                String op2 = calc.pop();
                String op1 = calc.pop();
                calc.push(String.valueOf(GetCalculate.calcSimpleEquation(Integer.parseInt(op1), Integer.parseInt(op2), tokenizer)));
            }
        } while (str.hasMoreTokens());
        return calc.pop();
    }

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

    private static boolean numberValue(String str) {
        if (str.charAt(0) == '-' && str.length() == 1)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-')
                continue;
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean varValue(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z') || str.length() != 1) {
                return false;
            }
        }
        return true;
    }

    private static int calcSimpleEquation(int op1, int op2, String op) {
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

    private static boolean isOperator(String op) {
        char c = op.charAt(0);
        return c == '+' || c == '-' || c == '/' || c == '*';
    }

    private static int priorityOfOperator(String op) {
        char c = op.charAt(0);
        if (c == '*' || c == '/') return 2;
        else if (c == '+' || c == '-') return 1;
        else return 0;
    }
}
