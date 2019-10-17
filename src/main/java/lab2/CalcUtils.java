package lab2;

import java.util.LinkedList;
import java.util.Scanner;

public class CalcUtils {
    public static void main(String[] ars) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println(deci(Scanner.nextLine()));
    }

    private static boolean ope(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int prior(char op) {
        switch (op) {
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

    private static void calc(LinkedList<Integer> st, char op) {
        int x = st.removeLast();
        int y = st.removeLast();
        switch (op) {
            case '+':
                st.add(x + y);
                break;
            case '-':
                st.add(y - x);
                break;
            case '*':
                st.add(x * y);
                break;
            case '/':
                st.add(y / x);
                break;
            default:
                break;
        }
    }

    public static int deci(String rts) {
        LinkedList<Integer> numb = new LinkedList<>();
        LinkedList<Character> op = new LinkedList<>();
        String s=rts.replaceAll("\\s+","");
        if (s.charAt(0)=='-')
            numb.add(0);
        for (int i = 0; i < s.length(); i++) {
            char p = s.charAt(i);
            if (p == '(') {
                op.add('(');
                if (s.charAt(i+1)=='-')
                    numb.add(0);
            }
            else if (p == ')') {
                while (op.getLast() != '(')
                    calc(numb, op.removeLast());
                op.removeLast();
            } else if (ope(p)) {
                while (!op.isEmpty() && prior(op.getLast()) >= prior(p))
                    calc(numb, op.removeLast());
                op.add(p);
            }  else {
                StringBuilder number = new StringBuilder();
                while (i < s.length() && Character.isDigit(s.charAt(i)))
                    number.append(s.charAt(i++));
                --i;
                numb.add(Integer.parseInt(number.toString()));
            }
        }
        while (!op.isEmpty())
            calc(numb, op.removeLast());
        return numb.get(0);
    }
}