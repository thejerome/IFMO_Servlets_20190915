import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

    public class CalcHelper {
    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println(answer(Scanner.nextLine()));
    }

    private static boolean operator(char var) {
        return var == '+' || var == '-' || var == '*' || var == '/';
    }

    private static int priority(char var) {
        if (var == '*' || var == '/')
            return 2;
        return 1;
    }

    private static String parse(String ln) {
        char tmp;
        char buffer;

        StringBuilder rlt = new StringBuilder();

        StringBuilder sb = new StringBuilder();
        for (int symbol = 0; symbol < ln.length(); ++symbol) {
            buffer = ln.charAt(symbol);

            if ('(' == buffer) {
                sb.append(buffer);
            }
            else if (')' == buffer) {
                tmp = sb.substring(sb.length() - 1).charAt(0);
                while ('(' != tmp) {
                    rlt.append(" ").append(tmp);
                    sb.setLength(sb.length() - 1);
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                }
                sb.setLength(sb.length() - 1);
            }
             else if (operator(buffer)) {
                while (sb.length() > 0) {
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                    if (operator(tmp) && (priority(buffer) <= priority(tmp))) {
                        rlt.append(" ").append(tmp).append(" ");
                        sb.setLength(sb.length() - 1);
                    } else {
                        rlt.append(" ");
                        break;
                    }
                }
                rlt.append(" ");
                sb.append(buffer);
            } else
                rlt.append(buffer);
        }
        while (sb.length() > 0) {
            rlt.append(" ").append(sb.substring(sb.length() - 1));
            sb.setLength(sb.length() - 1);
        }
        return rlt.toString();
    }

    public static int answer(String src) {
        int left;
        int right;
        Stack<Integer> stack = new Stack<>();
        String equation = parse(src);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken().trim();
            if (operator(tmp.charAt(0)) && 1 == tmp.length()) {
                right = stack.pop();
                left = stack.pop();
                switch (tmp.charAt(0)) {
                    case '+':
                        left += right;
                        break;
                    case '-':
                        left -= right;
                        break;

                    case '*':
                        left *= right;
                        break;
                    case '/':
                        left /= right;
                        break;

                    default:
                        break;
                }
                stack.push(left);
            } else {
                left = Integer.parseInt(tmp);
                stack.push(left);
            }
        }
        return stack.pop();
    }
}



