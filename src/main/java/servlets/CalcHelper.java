package servlets;
import java.util.Stack;
import java.util.StringTokenizer;

public class CalcHelper {
    public static boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static boolean isAnyOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == ' ';
    }

    public static String toPostfix(String infix) {
        char temp;
        StringBuilder result = new StringBuilder();
        StringBuilder hlp = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            if ('(' == infix.charAt(i)) {
                hlp.append(infix.charAt(i));
            } else if (')' == infix.charAt(i)) {
                temp = hlp.substring(hlp.length() - 1).charAt(0);
                while ('(' != temp) {
                    result.append(" ").append(temp);
                    hlp.setLength(hlp.length() - 1);
                    temp = hlp.substring(hlp.length() - 1).charAt(0);
                }
                hlp.setLength(hlp.length() - 1);
            } else if (isOperator(infix.charAt(i))) {
                while (hlp.length() != 0) {
                    temp = hlp.substring(hlp.length() - 1).charAt(0);
                    if (isOperator(temp) && (precedence(infix.charAt(i)) <= precedence(temp))) {
                        result.append(" " + temp + " ");
                        hlp.setLength(hlp.length() - 1);
                    } else {
                        break;
                    }
                }
                result.append(" ");
                hlp.append(infix.charAt(i));
            } else
                result.append(infix.charAt(i));
        }
        while (hlp.length() != 0) {
            result.append(" " + hlp.substring(hlp.length() - 1));
            hlp.setLength(hlp.length() - 1);
        }
        return result.toString();
    }

    public static int calculate(int left, int right, char operator) {
        switch (operator) {
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

    public static int precedence(char ch) {
        if (ch == '+' || ch == '-')
            return 1;
        return 2;
    }

    public static int calculation(String st) {
        int right;
        int left;
        Stack<Integer> stack = new Stack<>();
        String equation = toPostfix(st);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken();
            if (isOperator(tmp.charAt(0)) && 1 == tmp.length()) {
                right = stack.pop();
                left = stack.pop();
                left = calculate(left, right, tmp.charAt(0));
                stack.push(left);
            } else {
                left = Integer.parseInt(tmp);
                stack.push(left);
            }
        }
        return stack.pop();
    }

}
