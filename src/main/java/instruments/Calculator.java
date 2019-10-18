package instruments;

import java.util.*;

public class Calculator {
    public static boolean isDigit(String s){
        for (int i = 0; i < s.length(); ++i){
            if (s.charAt(i) < '0' || s.charAt(i) > '9'){
                return false;
            }
        }
        return true;
    }

    private static boolean isDelimeter(char c)
    {
        return (c == ' ' || c == '=');
    }

    private static boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '/' || c == '*');
    }


    public static String getExpression(String input)
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





    private static int getPriority(char symbol) {
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

    public static int counting(String input)
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
                if (a > 100000){
                    String aStr = Integer.toString(a).substring(4);
                    a = 0 - Integer.parseInt(aStr);
                }
                int b = temp.pop();
                if (b >= 100001){
                    String bStr = Integer.toString(b).substring(4);
                    b = 0 - Integer.parseInt(bStr);
                }

                switch (input.charAt(i)) //И производим над ними действие, согласно оператору
                {
                    case '+': result = b + a; break;
                    case '-': result = b - a; break;
                    case '*': result = b * a; break;
                    case '/': result = b / a; break;
                    default: result = 0; break;
                }
                temp.push(result); //Результат вычисления записываем обратно в стек
            }
        }

        return (temp.peek()); //Забираем результат всех вычислений из стека и возвращаем его
    }
}

