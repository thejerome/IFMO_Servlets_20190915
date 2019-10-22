package com.th1rt33nth.ifmo.web.servlets;

import java.util.*;
import java.util.Stack;
import java.util.ArrayDeque;
import java.util.regex.Pattern;

public class Auxilliary {
    public static int calc (String eq) {
        StringBuffer b = new StringBuffer('(' + eq + ')');
        int i = 1;
        while (i < b.length()) {
            if (b.charAt(i) == '-' && !Character.isDigit(b.charAt(i-1)))
                b.insert(i, '0');
            i++;
        }
        String s=b.toString();
        return backend(polskaKurva(s));
    }
    private static ArrayDeque<String> polskaKurva (String eq) {
        ArrayDeque<String> q = new ArrayDeque<>();
        Stack<Character> s = new Stack<>();
        int i = 0;
        while (i < eq.length()) {
            char ch = eq.charAt(i);
            if (Character.isDigit(ch)) {
                String num = "";
                while (Character.isDigit(eq.charAt(i))) {
                    num = num + eq.charAt(i);
                    i++;
                }
                q.offer(num);
                i--;
            }
            else if (ch == '(') {
                s.push(ch);
            }
            else if (ch == ')') {
                while (s.peek() != '(')
                    q.offer(s.pop().toString());
                s.pop();
            }
            else {
                while (s.peek() != '(' && !((ch == '*' || ch == '/') && (s
                        .peek() == '+' || s.peek() == '-')))
                    q.offer(s.pop().toString());
                s.push(ch);
            }
            i++;
        }
        while (!s.empty()) {
            q.offer(s.pop().toString());
        }
        return q;
    }
    private static int backend (ArrayDeque<String> q) {
        Stack<Integer> polStack = new Stack<>();
        while (!q.isEmpty()) {
            String qq = q.peek();
            if (Pattern.matches("^[0-9]+$", qq)) {
                polStack.push(Integer.parseInt(qq));
            } else {
                char op = q.peek().charAt(0);
                int b = polStack.pop();
                int a = polStack.pop();
                int res = 0;
                switch (op) {
                    case  ('+'):
                        res = a + b;
                        break;
                    case ('-'):
                        res = a - b;
                        break;
                    case ('*'):
                        res = a * b;
                        break;
                    case ('/'):
                        res = a / b;
                        break;
                    default:
                        //codacy, fuck you
                        res=res;
                        break;
                }
                polStack.push(res);
            }
            q.poll();
        }
        return polStack.pop();
    }
}
