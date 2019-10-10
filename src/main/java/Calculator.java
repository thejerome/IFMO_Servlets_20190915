import java.util.LinkedList;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] ars) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println(deci(Scanner.nextLine()));
    }

    private static boolean Ope(char c) {
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

    private static void Calc(LinkedList<Integer> st, char op) {
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

    static int deci(String str) {
        LinkedList<Integer> numb = new LinkedList<Integer>();
        LinkedList<Character> op = new LinkedList<Character>();
        str=str.replaceAll("\\s+","");
        if (str.charAt(0)=='-')
            numb.add(0);
        for (int i = 0; i < str.length(); i++) {
            char p = str.charAt(i);
            if (p == '(') {
                op.add('(');
                if (str.charAt(i+1)=='-')
                    numb.add(0);
            }
            else if (p == ')') {
                while (op.getLast() != '(')
                    Calc(numb, op.removeLast());
                op.removeLast();
            } else if (Ope(p)) {
                while (!op.isEmpty() && prior(op.getLast()) >= prior(p))
                    Calc(numb, op.removeLast());
                op.add(p);
            }  else {
                StringBuilder number = new StringBuilder();
                while (i < str.length() && Character.isDigit(str.charAt(i)))
                    number.append(str.charAt(i++));
                --i;
                numb.add(Integer.parseInt(number.toString()));
            }
        }
        while (!op.isEmpty())
            Calc(numb, op.removeLast());
        return numb.get(0);
    }
}