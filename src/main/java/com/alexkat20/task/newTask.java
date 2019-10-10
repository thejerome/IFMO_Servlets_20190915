package com.alexkat20.task;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet(
        name = "newTask", urlPatterns = {"/calc"})

public class newTask extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        String eq = request.getParameter("equation");
        Map<String, String[]> parameterMap = request.getParameterMap();

        StringBuilder example = new StringBuilder();
        for (int i = 0; i < eq.length(); ++i) {
            char cur = eq.charAt(i);
            if (cur >= 'a' && cur <= 'z') {
                String newVal = parameterMap.get(String.valueOf(cur))[0];
                while ( !isDigit(newVal) ){
                    newVal =  parameterMap.get(newVal)[0];
                }
                example.append(newVal);
            } else {
                example.append(eq.charAt(i));
            }
        }
        eq = example.toString();
        String exit = getExpression(eq);
        //out.println(equation);
        //out.println(exit);
        out.println(counting(exit));
        out.flush();
        out.close();
    }

    private boolean isDigit(String s){
        for (int i = 0; i < s.length(); ++i){
            if (s.charAt(i) < '0' || s.charAt(i) > '9'){
                return false;
            }
        }
        return true;
    }

    private boolean isDelimeter(char c)
    {
        return (c == ' ' || c == '=');
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '/' || c == '*');
    }


    private String getExpression(String input)
    {
        String output = ""; //Строка для хранения выражения
        Stack<Character> operStack = new Stack<>(); //Стек для хранения операторов

        for (int i = 0; i < input.length(); i++) //Для каждого символа в входной строке
        {
            //Разделители пропускаем
            if (isDelimeter(input.charAt(i)))
                continue; //Переходим к следующему символу

            //Если символ - цифра, то считываем все число
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') //Если цифра
            {
                //Читаем до разделителя или оператора, чтобы получить число
                while (!isDelimeter(input.charAt(i)) && !isOperator(input.charAt(i)))
                {
                    output += input.charAt(i); //Добавляем каждую цифру числа к нашей строке
                    i++; //Переходим к следующему символу

                    if (i == input.length()) break; //Если символ - последний, то выходим из цикла
                }

                output += " "; //Дописываем после числа пробел в строку с выражением
                i--; //Возвращаемся на один символ назад, к символу перед разделителем
            }

            //Если символ - оператор
            if (isOperator(input.charAt(i)) | input.charAt(i) == '(' | input.charAt(i) == ')') //Если оператор
            {
                if (input.charAt(i) == '(') //Если символ - открывающая скобка
                    operStack.push(input.charAt(i)); //Записываем её в стек
                else if (input.charAt(i) == ')') //Если символ - закрывающая скобка
                {
                    //Выписываем все операторы до открывающей скобки в строку
                    char s = operStack.pop();

                    while (s != '(')
                    {
                        output += (Character.toString(s) + ' ');
                        s = operStack.pop();
                    }
                }
                else //Если любой другой оператор
                {
                    if (operStack.size() > 0 && getPriority(input.charAt(i)) <= getPriority(operStack.peek())) //Если в стеке есть элементы, и если приоритет нашего оператора меньше или равен приоритету оператора на вершине стека
                            output += (operStack.pop().toString() + " ");//То добавляем последний оператор из стека в строку с выражением



                    operStack.push(input.charAt(i)); //Если стек пуст, или же приоритет оператора выше - добавляем операторов на вершину стека

                }
            }
        }

        //Когда прошли по всем символам, выкидываем из стека все оставшиеся там операторы в строку
        while (operStack.size() > 0)
            output += (operStack.pop() + " ");

        String result = "";
        for (int i = 0 ; i < output.length(); i ++){
            if (output.charAt(i) != ')')
                result += output.charAt(i);
        }
        return result; //Возвращаем выражение в постфиксной записи
    }





    private int getPriority(char symbol) {
        switch (symbol)
        {
            case '(': return 0;
            case ')': return 1;
            case '+': return 2;
            case '-': return 3;
            case '*': return 4;
            case '/': return 4;
            default: return 5;
        }
    }

    private int counting(String input)
    {
        int result = 0; //Результат
        Stack<Integer> temp = new Stack<>(); //стек для решения

        for (int i = 0; i < input.length(); i++) //Для каждого символа в строке
        {
            //Если символ - цифра, то читаем все число и записываем на вершину стека
            if (input.charAt(i) >= '0' && input.charAt(i)<= '9')
            {
                String a = "";

                while (!isDelimeter(input.charAt(i)) && !isOperator(input.charAt(i))) //Пока не разделитель
                {
                    a += input.charAt(i); //Добавляем
                    i++;
                    if (i == input.length()) break;
                }
                temp.push(Integer.parseInt(a)); //Записываем в стек
                i--;
            }
            else if (isOperator(input.charAt(i))) //Если символ - оператор
            {
                //Берем два последних значения из стека
                int a = temp.pop();
                int b = temp.pop();

                switch (input.charAt(i)) //И производим над ними действие, согласно оператору
                {
                    case '+': result = b + a; break;
                    case '-': result = b - a; break;
                    case '*': result = b * a; break;
                    case '/': result = b / a; break;
                    default: result = 0;
                }
                temp.push(result); //Результат вычисления записываем обратно в стек
            }
        }

        return (temp.peek()); //Забираем результат всех вычислений из стека и возвращаем его
    }
}
