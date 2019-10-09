import java.util.LinkedList;
import java.util.Scanner;
import java.util.HashMap;

public class Calculator {
    public static void main(String[] ars) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println(decis(Scanner.nextLine()));
    }

    static boolean Oper(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    static int prio(char op) {
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

    static void Calcul(LinkedList<Integer> st, char op) {
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
        }
    }

    public static int decis(String s) {
        LinkedList<Integer> numb = new LinkedList<Integer>();
        LinkedList<Character> op = new LinkedList<Character>();
        HashMap<Character, Integer> map = new HashMap<>();
        s=s.replaceAll("\\s+","");
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
                    Calcul(numb, op.removeLast());
                op.removeLast();
            } else if (Oper(p)) {
                while (!op.isEmpty() && prio(op.getLast()) >= prio(p))
                    Calcul(numb, op.removeLast());
                op.add(p);
            } else if(Character.isLetter(p)){
                numb.add(map.get(p));
            } else {
                String number = "";
                while (i < s.length() && Character.isDigit(s.charAt(i)))
                    number += s.charAt(i++);
                --i;
                numb.add(Integer.parseInt(number));
            }
        }
        while (!op.isEmpty())
            Calcul(numb, op.removeLast());
        return numb.get(0);
    }
}