package org.mrkaschenko.helpers;

public class Helper {

    public static boolean isInteger(String stringToCheck) {
        try {
            Integer.parseInt(stringToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    public static int getPrecedence(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    public static boolean isOperand(char ch) {
        return (ch >= 'a' && ch <= 'z') ||
               (ch >= 'A' && ch <= 'Z') ||
               (ch >= '0' && ch <='9');
    }
}
