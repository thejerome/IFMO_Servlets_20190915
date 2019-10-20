package com.esina.ifmo.web.servlets;

import java.util.ArrayDeque;
import java.util.Stack;
import java.util.regex.Pattern;

// Статический класс-калькулятор, берет чистую строку с выражением и отдает число

public class CalculatorUtil {
    public static int calculate (String toCalc) {

        // 1. Перевод строки выражения в очередь в обратной польской нотации
        String eq = '(' + toCalc + ')';

        // Лечим унарный минус, вставляя перед ним 0
        int i = 1;
        StringBuffer buff = new StringBuffer(eq);
        while (i < buff.length()) {
            if (buff.charAt(i) == '-' && !Character.isDigit(buff.charAt(i-1)))
                buff.insert(i, '0');
            i++;
        }
        eq = buff.toString();

        ArrayDeque<String> queue = new ArrayDeque<>();
        Stack<Character> eqStack = new Stack<>();

        i = 0;
        while (i < eq.length()) {
            char ch = eq.charAt(i);

            // Если символ - число, добавляем в очередь
            if (Character.isDigit(ch)) {
                String num = "";


                while (Character.isDigit(eq.charAt(i))) {
                    num = num + eq.charAt(i);
                    i++;
                }

                queue.offer(num);
                i--;
            }


            // Если символ - открывающая скобка, добавляем в стек
            else if (ch == '(') {
                eqStack.push(ch);
            }
            // Если символ - закрывающая скобка, выталкиваем стек в очередь до открывающей скобки
            else if (ch == ')') {
                while (eqStack.peek() != '(')
                    queue.offer(eqStack.pop().toString());

                eqStack.pop();
            }

            // Если символ - операция, выталкиваем все операции с неменьшим приоритетом в очередь и помещаем эту в стек
            else {
                while (eqStack.peek() != '(' && !((ch == '*' || ch == '/') && (eqStack
                        .peek() == '+' || eqStack.peek() == '-')))
                    queue.offer(eqStack.pop().toString());

                eqStack.push(ch);
            }

            i++;
        }

        // Выталкиваем в очередь все оставшиеся элементы стека
        while (!eqStack.empty()) {
            queue.offer(eqStack.pop().toString());
        }


        // 2. Вычисление выражения в обратной польской нотации
        Stack<Integer> polStack = new Stack<>();

        while (!queue.isEmpty()) {
            String q = queue.peek();

            if (Pattern.matches("^[0-9]+$", q)) {
                polStack.push(Integer.parseInt(q));
            }
            else {
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