package com.graf.ifmo.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "С",
        urlPatterns = "/calc/усморжовый"
)


public class C extends HttpServlet {

    public static int calc(String equation) {
        return doParse(tHIS(equation));
    }

    //преобразует обычную запись в обратную польскую нотацию
    private static Queue<String> tHIS(String line) {

        Queue<String> output = new ArrayDeque<>();
        Stack<Character> stack = new Stack<>();

        String trueline = ("(" + line + ")").replaceAll(" ", "").replaceAll("\\D-", "0-");

        int size = trueline.length();

        int i = 0;
        while (i < size) {
            char el = trueline.charAt(i);

            switch (el) {
                case '+':
                case '-':
                    while (!stack.isEmpty()) {
                        char opTop = stack.pop();
                        if (opTop == '(') {
                            stack.push(opTop);
                            break;
                        } else
                            output.offer("" + opTop);

                    }
                    stack.push(el);
                    break;
                case '*':
                case '/':
                    while (!stack.isEmpty()) {
                        char opTop = stack.pop();
                        if (opTop == '(') {
                            stack.push(opTop);
                            break;
                        } else {
                            int oldPriority;
                            if (opTop == '+' || opTop == '-')
                                oldPriority = 1;
                            else
                                oldPriority = 2;

                            if (oldPriority < 2) {
                                stack.push(opTop);
                                break;
                            } else
                                output.offer("" + opTop);
                        }
                    }
                    stack.push(el);
                    break;
                case '(':
                    stack.push(el);
                    break;
                case ')':
                    while (!stack.isEmpty()) {
                        char elx = stack.pop();
                        if (elx == '(')
                            break;
                        else
                            output.offer("" + elx);
                    }
                    break;
                default:
                    String chislo;
                    int beg = i;

                    while (Character.isDigit(trueline.charAt(i)))
                        i++;

                    chislo = trueline.substring(beg, i);
                    output.offer(chislo);
                    i--;
                    break;
            }
            i++;
        }

        while (!stack.isEmpty())
            output.offer(stack.pop().toString());
        return output;
    }

    // квасит выражение до интового ответа
    private static int doParse(Queue<String> queue) {

        Stack<Integer> polStack = new Stack<>();
        while (!queue.isEmpty()) {
            String q = queue.peek();
            if (Pattern.matches("^[0-9]+$", q)) {
                polStack.push(Integer.parseInt(q));
            } else {
                assert queue.peek() != null;
                char op = queue.peek().charAt(0);
                int b = polStack.pop();
                int a = polStack.pop();
                int res = 0;
                if (op == '+')
                    res = a + b;
                else if (op == '-')
                    res = a - b;
                else if (op == '*')
                    res = a * b;
                else if (op == '/')
                    res = a / b;
                polStack.push(res);
            }
            queue.poll();
        }
        return polStack.pop();
    }

}