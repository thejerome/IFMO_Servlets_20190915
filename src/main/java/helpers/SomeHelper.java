package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SomeHelper {

    public static boolean isExpression(String s) {
        Pattern pattern = Pattern.compile("^[(]*[a-z0-9]?([-+/*][(]*[a-z0-9][)]*)*$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean isValue(String val) {
        Pattern pattern = Pattern.compile("^[a-z]$");
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isAnyOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedence(char ch) {
        if (ch == '+' || ch == '-')
            return 1;
        return 2;
    }

     public static int calculate(int left, int right, char op) {
        switch (op) {
            case '+':
                return left + right;
            case '-':
                return left - right;
            case '*':
                return left * right;
            case '/':
                return left / right;
            default:
                return 0;
        }
    }
}
