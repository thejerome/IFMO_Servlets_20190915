package com.alexkat20.web.servlets;

import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (urlPatterns = {"/calc/result"})


public class Result extends HttpServlet{
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
        String output = "";
        Stack<Character> operStack = new Stack<>();

        for (int i = 0; i < input.length(); i++)
        {

            if (isDelimeter(input.charAt(i)))
                continue;


            if (input.charAt(i) >= '0' && input.charAt(i) <= '9')
            {

                while (!isDelimeter(input.charAt(i)) && !isOperator(input.charAt(i)))
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
                    operStack.push(input.charAt(i));
                else if (input.charAt(i) == ')')
                {
                    char s = operStack.pop();

                    while (s != '(')
                    {
                        output += (Character.toString(s) + ' ');
                        s = operStack.pop();
                    }
                }
                else
                {
                    if (operStack.size() > 0 && getPriority(input.charAt(i)) <= getPriority(operStack.peek()))
                        output += (operStack.pop().toString() + " ");



                    operStack.push(input.charAt(i));

                }
            }
        }

        while (operStack.size() > 0)
            output += (operStack.pop() + " ");

        String result = "";
        for (int i = 0 ; i < output.length(); i ++){
            if (output.charAt(i) != ')')
                result += output.charAt(i);
        }
        return result;
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
        int result = 0;
        Stack<Integer> temp = new Stack<>();

        for (int i = 0; i < input.length(); i++)
        {

            if (input.charAt(i) >= '0' && input.charAt(i)<= '9')
            {
                String a = "";

                while (!isDelimeter(input.charAt(i)) && !isOperator(input.charAt(i)))
                {
                    a += input.charAt(i);
                    i++;
                    if (i == input.length()) break;
                }
                temp.push(Integer.parseInt(a));
                i--;
            }
            else if (isOperator(input.charAt(i)))
            {
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

                switch (input.charAt(i))
                {
                    case '+': result = b + a; break;
                    case '-': result = b - a; break;
                    case '*': result = b * a; break;
                    case '/': result = b / a; break;
                    default: result = 0; break;
                }
                temp.push(result);
            }
        }

        return (temp.peek());
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String eq = (String) session.getAttribute("equation");

        if (eq != null) {

            String ans;
            Map<String, String> parameterMap = new HashMap<>();
            for (String s : session.getValueNames()) {
                if (s != "equation") {
                    String v = (String) session.getAttribute(s);
                    if (v.charAt(0) == '-') {
                        ans = "10000" + v.substring(1);
                        parameterMap.put(s, ans);
                    } else {
                        parameterMap.put(s, v);
                    }


                }


            }


            StringBuilder example = new StringBuilder();



            for (int i = 0; i < eq.length(); ++i) {
                char cur = eq.charAt(i);
                if (cur >= 'a' && cur <= 'z') {
                    String newVal = parameterMap.get(Character.toString(cur));
                    if (newVal == null) break;
                    while (!isDigit(newVal)) {
                        newVal = parameterMap.get(newVal);

                    }
                    example.append(newVal);
                } else {
                    example.append(eq.charAt(i));
                }

            }

            eq = example.toString();


            try {
                out.print(counting(getExpression(eq)));
                response.setStatus(200);
            }
            catch (Exception e){
                response.setStatus(409);
                out.println("lack of data");
            }
        }
        else {
            response.setStatus(409);
            response.getWriter().println("lack of data");
        }
    }
}
