package com.esina.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "ServletCalc",
        urlPatterns = "/calc"
)
public class ServletCalc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        final Map<String, String[]> parameterMap= req.getParameterMap();

        // Убираем пробелы и вставляем значения переменных в выражение
        String eq = req.getParameter("equation").replace(" ", "");
        while (!Pattern.matches("^[0-9*+-/()]+$", eq)) {
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
                eq = eq.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }

        // Вычисляем и выводим результат выражения
        out.println(calculate(eq));

        out.flush();
        out.close();
    }

    int calculate (String eq) {
        // 1. Перевод строки выражения в очередь в обратной польской нотации
        eq = '(' + eq + ')';

        ArrayDeque<String> queue = new ArrayDeque<>();
        Stack<Character> eqStack = new Stack<>();

        for (int i = 0; i < eq.length(); i++) {
            char ch = eq.charAt(i);

            // Если символ - число, добавляем в очередь
            if (Character.isDigit(ch)) {
                String num = "";

                for (; Character.isDigit(eq.charAt(i)); i++)
                    num = num + eq.charAt(i);

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