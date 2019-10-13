package com.ogalay.task;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet (
        name = "Calculator",
        urlPatterns = {"/calc"}
)
public class Calculator extends HttpServlet{

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        Map<String, String[]> parMap = req.getParameterMap();

        StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < equation.length(); i++){
                char sym = equation.charAt(i);
                if (sym >= 'a' && sym <= 'z') {
                    String newValue = parMap.get(String.valueOf(sym))[0];
                    while (isLetter(newValue)) {
                        newValue = parMap.get(newValue)[0];
                    }
                    stringBuilder.append(newValue);
                }
                else {
                    stringBuilder.append(equation.charAt(i));
                }
            }

        equation = stringBuilder.toString();
        String exit = getExpression(equation);
        out.println(counting(exit));
        out.flush();
            
        out.close();
    }

    private boolean isLetter(String s){
        for (int i = 0; i < s.length(); ++i){
            if (Character.isLetter(s.charAt(i))){
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '/' || c == '*');
    }

    private static int getPriority(char sym) {
        switch (sym)
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

    public String getExpression(String input)
    {
        String output = "";
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < input.length(); i++)
        {

            if (input.charAt(i) == ' ')
                continue;

            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                while (input.charAt(i) != ' ' && !isOperator(input.charAt(i))) //Смотрим до разделения, чтобы получить число
                {
                    output += input.charAt(i);
                    i++;

                    if (i == input.length()) break;
                }

                output += " ";
                i--;
            }

            if (isOperator(input.charAt(i)) | input.charAt(i) == '(' | input.charAt(i) == ')')
            {
                if (input.charAt(i) == '(')
                    operators.push(input.charAt(i));
                else if (input.charAt(i) == ')')
                {
                    char s = operators.pop();

                    while (s != '(')
                    {
                        output += (Character.toString(s) + ' ');
                        s = operators.pop();
                    }
                }
                else
                {
                    if (operators.size() > 0 && getPriority(input.charAt(i)) <= getPriority(operators.peek())) {
                            output += (operators.pop().toString() + " ");
                    }
                    operators.push(input.charAt(i));

                }
            }
        }

        while (operators.size() > 0)
            output += (operators.pop() + " ");

        String result = "";
        for (int i = 0 ; i < output.length(); i ++){
            if (output.charAt(i) != ')')
                result += output.charAt(i);
        }
        return result;
    }

    public int counting(String input)
    {
        int result = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < input.length(); i++)
        {

            if (input.charAt(i) >= '0' && input.charAt(i)<= '9')
            {
                String a = "";

                while (input.charAt(i) != ' ' && !isOperator(input.charAt(i))) {
                    a += input.charAt(i);
                    i++;
                    if (i == input.length()) break;
                }
                stack.push(Integer.parseInt(a));
                i--;
            }
            else if (isOperator(input.charAt(i)))
            {
                int a = stack.pop();
                int b = stack.pop();

                if (input.charAt(i) == '+') stack.push(b + a);
                else if (input.charAt(i) == '-') stack.push(b - a);
                else if (input.charAt(i) == '*') stack.push(b * a);
                else if (input.charAt(i) == '/') stack.push(b / a);
            }
        }

        return (stack.peek());
    }
}


