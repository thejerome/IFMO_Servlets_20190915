package com.ogalay.task.web;

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


public class ServletResult extends HttpServlet{
    public static boolean isLetter(String s){
        for (int i = 0; i < s.length(); ++i){
            if (Character.isLetter(s.charAt(i))){
                return true;
            }
        }
        return false;
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

            if (input.charAt(i) == ' ')
                continue;


            if (input.charAt(i) >= '0' && input.charAt(i) <= '9')
            {

                while (input.charAt(i) != ' ' && !isOperator(input.charAt(i)))
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
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < input.length(); i++)
        {

            if (input.charAt(i) >= '0' && input.charAt(i)<= '9')
            {
                String a = "";

                while (input.charAt(i) != ' ' && !isOperator(input.charAt(i)))
                {
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
                if (a > 100000){
                    a = 0 - a / 10000;
                }
                int b = stack.pop();
                if (b > 100000){
                    b = 0 - b / 10000;
                }
                if (input.charAt(i) == '+') stack.push(b + a);
                else if (input.charAt(i) == '-') stack.push(b - a);
                else if (input.charAt(i) == '*') stack.push(b * a);
                else if (input.charAt(i) == '/') stack.push(b / a);

            }
        }

        return (stack.peek());
    }



    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        String eq = (String) session.getAttribute("equation");

        if (eq != null) {
            String ans;
            Map<String, String> parameterMap = new HashMap<>();
            for (String s : session.getValueNames()) {
                if (!s.equals("equation")) {
                    String v = (String) session.getAttribute(s);
                    if (v.charAt(0) == '-') {
                        ans = v.substring(1) + "0000";
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
                    while (isLetter(newVal)) {
                        newVal = parameterMap.get(newVal);

                    }
                    example.append(newVal);
                } else {
                    example.append(eq.charAt(i));
                }

            }
            eq = example.toString();
            try {
                //out.println(eq);
                //out.println(getExpression(eq));
                out.print(counting(getExpression(eq)));
                resp.setStatus(200);
            }
            catch (Exception e){
                resp.setStatus(409);
                out.println("lack of data");
            }
        }
        else {
            resp.setStatus(409);
            resp.getWriter().println("lack of data");
            //forCommit
        }
    }
}