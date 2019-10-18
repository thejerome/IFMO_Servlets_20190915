package com.marycia.web.servlets;
import java.util.*;

public class PerserUtil {
    private static String delimiters = "() +=*/";

    private static boolean isDelimiters (String tmp) {
        for (int i = 0; i < delimiters.length(); i++) {
            if (tmp.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }


    private static int priority (String tmp) {
        if ("(".equals(tmp)) return 1;
        if ("+".equals(tmp) || "-".equals(tmp)) return 2;
        return 3;
    }

    public static List<String> parse (String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String curr;
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (isDelimiters(curr)) {
                if ("(".equals(curr)) stack.push(curr);
                else if (")".equals(curr)) {
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
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }

}
