import java.util.*;
import java.lang.*;
import java.io.*;

public class Parser {
    private static String operators = "+-*/";
    private static String delimiters = "() " + operators;
    public static boolean flag = true;

    private static boolean isDelimiters (String tmp) {
        for (int i = 0; i < delimiters.length(); i++) {
            if (tmp.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isOperator (String tmp) {
        for (int i = 0; i < operators.length(); i++) {
            if (tmp.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private static int priority (String tmp) {
        if (tmp.equals("(")) return 1;
        if (tmp.equals("+") || tmp.equals("-")) return 2;
        if (tmp.equals("*") || tmp.equals("/")) return 3;
        return 4; //Подумать, нужно ли 4, можно ли обойтись тремя, у нас нет функций
    }

    public static List<String> parse (String infix) {
        List<String> postfix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (isDelimiters(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                    if (!stack.isEmpty()) {
                        postfix.add(stack.pop());
                    }
                } else  {
                    while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                        postfix.add(stack.pop());
                    }
                    stack.push(curr);
                }
            } else {
                postfix.add(curr);
            }

            prev = curr;
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }
}


