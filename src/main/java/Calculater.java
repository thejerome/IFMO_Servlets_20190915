import java.util.ArrayDeque;
import java.util.Deque;

public class Calculater {
    private Deque<String> stack = new ArrayDeque<>();
    private Deque<String> nstack = new ArrayDeque<>();
    private int prev = 0;
    private void negativenumprocessor(Deque stack, Deque nstack) {
        while (!nstack.isEmpty())
            if (nstack.peekLast().equals("sub")) {
                int k = -1 * Integer.parseInt((String) stack.peekFirst());
                stack.pop();
                stack.push(String.valueOf(k));
                nstack.removeLast();
            }
            else {
                stack.push(nstack.peekLast());
                nstack.removeLast();}
        while (!stack.isEmpty()) {
            nstack.push(stack.peekLast());
            stack.removeLast();
        }
    }

    public int calculate(String s) {
        char[] modequation = s.toCharArray();
        equationtostack(modequation);
        negativenumprocessor(stack, nstack);
        int leftoperand = 0;
        while (!nstack.isEmpty()) {
            leftoperand = Integer.parseInt(String.valueOf(nstack.peekFirst()));
            stack.push(nstack.peekFirst());
            nstack.pop();
            int rightoperand;
            if (!nstack.isEmpty()) {
                rightoperand = Integer.parseInt(String.valueOf(nstack.peekFirst()));
            } else
                break;
            stack.push(nstack.peekFirst());
            nstack.pop();
            while (!nstack.isEmpty() && isNum(nstack.peekFirst())) {
                leftoperand = rightoperand;
                rightoperand = Integer.parseInt(String.valueOf(nstack.peekFirst()));
                stack.push(nstack.peekFirst());
                nstack.pop();
            }
            switch (nstack.peekFirst()) {
                case ("+"):
                    leftoperand += rightoperand;
                    break;
                case ("-"):
                    leftoperand -= rightoperand;
                    break;
                case ("*"):
                    leftoperand *= rightoperand;
                    break;
                case ("/"):
                    leftoperand = leftoperand / rightoperand;
                    break;
                default:
                    break;
            }
            nstack.pop();
            nstack.push(String.valueOf(leftoperand));
            stack.pop();
            stack.pop();
            while (!stack.isEmpty()) {
                nstack.push(stack.peekFirst());
                stack.pop();
            }
        }
        return leftoperand;
    }

    private void equationtostack(char[] modequation) {
        for (char c : modequation) {
            numeral(c);
            addandsub(c);
            multanddiv(c);
            brackets(c);
        }
        while (!stack.isEmpty()) {
            nstack.push(stack.peekFirst());
            stack.pop();
        }
    }
    private void stackaction() {
        if (!nstack.isEmpty()) {
            stack.push(nstack.peekFirst());
            nstack.pop();
        }
    }

    private boolean equalstofirst(String s) {
        return nstack.peekFirst().equals(s);
    }

    private void brackets(char c) {
        if (c == '(') {
            nstack.push("(");
            prev = 0;
        }
        if (c == ')') {
            prev = 0;
            do {
                stackaction();
            } while (!equalstofirst("("));
            nstack.pop();
        }
    }

    private void multanddiv(char c) {
        if (c == '*' || c == '/') {
            if ((!nstack.isEmpty()) && (equalstofirst("*") || equalstofirst("/"))) {
                stackaction();
            }
            nstack.push(String.valueOf(c));
            prev = 0;
        }
    }

    private void addandsub(char c) {
        if (c == '+' || c == '-') {
            if (prev == 0 && c == '-')
                stack.push("sub");
            else if ((!nstack.isEmpty()) && (equalstofirst("+") || equalstofirst("-") ||
                    equalstofirst("*") || equalstofirst("/"))) {
                stackaction();
                nstack.push(String.valueOf(c));
            }
            else
                nstack.push(String.valueOf(c));
            prev = 0;
        }
    }

    private void numeral(char c) {
        if ('0' <= c && c <= '9') {
            int j;
            if (!stack.isEmpty() && prev == 1) {
                if (isNum(stack.peekFirst())) {
                    j = Integer.parseInt(stack.peekFirst().trim());
                    stack.pop();
                    stack.push(j + String.valueOf(c));
                    prev = 1;
                }
            } else {
                stack.push(String.valueOf(c));
                prev = 1;
            }
        }
    }


    private boolean isNum(String strNum) {
        for (int i = 0; i < strNum.length(); i++) {
            char c = strNum.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;

    }




}
