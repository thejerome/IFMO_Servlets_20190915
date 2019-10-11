import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculator {
    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println(calc(Scanner.nextLine()));
    }

    private static boolean Operator(char x) {
        return x == '+' || x == '-' || x == '*' || x == '/';
    }

    private static int priority(char x) {
        if (x == '*' || x == '/')
            return 2;
        return 1;
    }

    private static String pol(String src) {
        char tmp;
        char buffer;

        StringBuilder res = new StringBuilder();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            buffer = src.charAt(i);
            if (Operator(buffer)) {
                while (sb.length() > 0) {
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                    if (Operator(tmp) && (priority(buffer) <= priority(tmp))) {
                        res.append(" ").append(tmp).append(" ");
                        sb.setLength(sb.length() - 1);
                    } else {
                        res.append(" ");
                        break;
                    }
                }
                res.append(" ");
                sb.append(buffer);
            } else if ('(' == buffer) {
                sb.append(buffer);
            }
            else if (')' == buffer) {
                tmp = sb.substring(sb.length() - 1).charAt(0);
                while ('(' != tmp) {
                    res.append(" ").append(tmp);
                    sb.setLength(sb.length() - 1);
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                }
                sb.setLength(sb.length() - 1);
            } else
                res.append(buffer);
        }
        while (sb.length() > 0) {
            res.append(" ").append(sb.substring(sb.length() - 1));
            sb.setLength(sb.length() - 1);
        }
        return res.toString();
    }

    static int calc(String st) {
        int lft;
        int rht;
        Stack<Integer> stack = new Stack<>();
        String equation = pol(st);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken().trim();
            if (Operator(tmp.charAt(0)) && 1 == tmp.length()) {
                rht = stack.pop();
                lft = stack.pop();
                switch (tmp.charAt(0)) {
                    case '+':
                        lft += rht;
                        break;
                    case '*':
                        lft *= rht;
                        break;

                    case '-':
                        lft -= rht;
                        break;
                    case '/':
                        lft /= rht;
                        break;

                    default:
                        break;
                }
                stack.push(lft);
            } else {
                lft = Integer.parseInt(tmp);
                stack.push(lft);
            }
        }
        return stack.pop();
    }
}



