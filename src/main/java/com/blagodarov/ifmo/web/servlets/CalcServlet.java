package com.blagodarov.ifmo.web.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.regex.Pattern;

@WebServlet(
    name = "CalcServlet",
    urlPatterns = {"/calc"}
)
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        
        final Map<String, String[]> parameterMap = request.getParameterMap();

        String equation = request.getParameter("equation").replace(" ", "");
        
        while (!Pattern.matches("^[0-9*+-/()]+$", equation)){
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()){
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }
        writer.println(calc(equation));
        writer.flush();
        writer.close();
    }
    private static int calc(String expression) {
        char[] equation = expression.toCharArray();

        // Stack for numbers
        Stack<Integer> values = new Stack<Integer>();

        // Stack for operators 
        Stack<Character> ops = new Stack<Character>();
        
        for (int i = 0; i < equation.length; i++) {
             char symbol = equation[i];
             
            // If we are on a number
            if (isNumber(symbol)) {
                StringBuilder newNumber = new StringBuilder();
                while (i < equation.length && isNumber(equation[i])) //If number is big
                    newNumber.append(equation[i++]);
                values.push(Integer.parseInt(newNumber.toString()));
                i--;
            }

            //if bracket starts
            else if (symbol == '(') {
                ops.push(symbol);
            }

            //if bracket ends, and also calc
            else if (symbol == ')') {
                while (ops.peek()!= '(') {
                    int ans = oneOp(values.pop(), ops.pop(), values.pop());
                    values.push(ans);
                }
                ops.pop();
            }


            //if operator
            else if (isOp(symbol)){
                while (!ops.empty() && importance(symbol) <= importance(ops.peek())) {
                    int ans = oneOp(values.pop(), ops.pop(), values.pop());
                    values.push(ans);
                }
                ops.push(equation[i]);
            }
        }
        //final calculations when no brackets left
        while (!ops.empty()) {
            int ans = oneOp(values.pop(), ops.pop(), values.pop());
            values.push(ans);
        }
        //the only left is the result 
        return values.pop();
    }
    

    //makes order correct
    private static int importance(char op){
        switch(op){
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '0':
                return 3;
            default:
                return 0;
        }
    }
    private static boolean isOp(char c){
        switch (c){
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
            default:
                return false;
        }
    }
    
    private static boolean isNumber(char c){
        return (c >= '0' && c <= '9');
    }

    //basic operation with two numbers
    private static int oneOp(int b, char op, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                return 0;
        }
    }

}

