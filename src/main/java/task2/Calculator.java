package task2;

import java.util.*;
import java.util.regex.Pattern;

public class Calculator {

    public static int getResult(String equation, Map<String, Object> var) {
        String clearEquation = clear(equation, var);
        if (contains(clearEquation, var)) {
            throw new IllegalArgumentException(equation);
        }
        System.out.println("Cleared: " + clearEquation);
        if (uncleaned(clearEquation))
            throw new IllegalArgumentException();
        return answer(post(tok(clearEquation)));
    }

    private static boolean uncleaned(String str) {
        for (char i : str.toCharArray()) {
            if (i >= 'a' && i <= 'z') {
                return true;
            }
        }
        return false;
    }

    private static String clear(String expr, Map<String, Object> var) {
        List<String> keysToDelete = new ArrayList<>();
        for (Map.Entry<String, Object> kek : var.entrySet()) {
            System.out.println(expr);
            if (!(kek.getValue() instanceof Integer)) {
                String newStr = expr.replaceAll(kek.getKey(), (String) kek.getValue());
                if (!newStr.equals(expr))
                    keysToDelete.add(kek.getKey());
                expr = newStr;
            } else {
                String newStr = expr.replaceAll(kek.getKey(), kek.getValue().toString());
                if (!newStr.equals(expr))
                    keysToDelete.add(kek.getKey());
                expr = newStr;
            }
            System.out.println(expr);
        }
        System.out.println(var);
        if (contains(expr, var)) {
            return clear(expr, var);
        }
        return expr;
    }

    private static boolean contains(String str, Map<String, Object> var) {
        boolean contains = false;
        char chr = 'a';
        for (char i : str.toCharArray()) {
            if (i >= 'a' && i <= 'z') {
                contains = true;
                chr = i;
                break;
            }
        }
        if (var.get(String.valueOf(chr)) == null)
            return false;
        return contains;
    }

    private static ArrayList<String> tok(String expression) {
        String expr = expression.replaceAll(" ", "");
        String[] lets = expr.split("");
        StringBuffer buf = new StringBuffer();
        List<String> kek = new LinkedList<>();

        for (int i = 0; i < lets.length; i++) {
            if (i == 0 && lets[i].equals("-")) {
                buf.append("-");
            } else if (lets[i].equals("-") && Pattern.matches("[+*/()]", lets[i - 1])) {
                buf.append("-");
            } else if (Pattern.matches("[0-9]", lets[i])) {
                buf.append(lets[i]);
            } else if (Pattern.matches("[-+*/()]", lets[i])) {
                if (buf.length() > 0) {
                    kek.add(buf.toString());
                    buf.delete(0, buf.length());
                }
                kek.add(lets[i]);
            } else if (Pattern.matches("[a-z]", lets[i])) {
                kek.add(lets[i]);
            }
            if (i == lets.length - 1 && buf.length() > 0) {
                kek.add(buf.toString());
                buf.delete(0, buf.length());
            }
        }

        ArrayList<String> list = new ArrayList<>();
        for (String i : kek)
            list.add(i);
        return list;
    }

    private static String[] post(ArrayList<String> toks) {
        StringBuffer out = new StringBuffer();
        Deque<String> stk = new ArrayDeque<>();

        for (String token : toks) {
            if (operators.containsKey(token)) {
                while (!stk.isEmpty() && prec(token, stk.peek()))
                    out.append(stk.pop()).append(' ');
                stk.push(token);
            } else if ("(".equals(token)) {
                stk.push(token);
            } else if (")".equals(token)) {
                while (!"(".equals(stk.peek()))
                    out.append(stk.pop()).append(' ');
                stk.pop();
            } else {
                out.append(token).append(' ');
            }
        }

        while (!stk.isEmpty())
            out.append(stk.pop()).append(' ');

        return out.toString().split(" ");
    }

    private enum op {
        PLUS(1), MINUS(1), STAR(2), NOTSTAR(2);
        final int prec;

        op(int p) {
            prec = p;
        }
    }

    private static Map<String, op> operators = new HashMap<String, op>() {{
        put("+", op.PLUS);
        put("-", op.MINUS);
        put("*", op.STAR);
        put("/", op.NOTSTAR);
    }};

    private static boolean prec(String op, String sub) {
        return (operators.containsKey(sub) && operators.get(sub).prec >= operators.get(op).prec);
    }

    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int calcMiddle(String op, int num1, int num2) {
        switch (op) {
            case "+":
                return num2 + num1;
            case "-":
                return num2 - num1;
            case "*":
                return num2 * num1;
            case "/":
                return num2 / num1;
        }
        throw new IllegalArgumentException();
    }

    private static int answer(String[] toks) {
        Deque<String> ctack = new ArrayDeque<>();
        for (String token : toks) {
            if (isNumber(token)) {
                ctack.push(token);
            } else if (operators.containsKey(token)) {
                int num1 = Integer.parseInt(ctack.pop());
                int num2 = Integer.parseInt(ctack.pop());
                int result = calcMiddle(token, num1, num2);

                ctack.push(String.valueOf(result));
            }
        }
        return Integer.parseInt(ctack.pop());
    }
}
