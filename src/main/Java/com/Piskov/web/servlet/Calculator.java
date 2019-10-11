package com.Piskov.web.servlet;
import java.util.*;
import java.lang.*;

class Calculator {
    public static String expression_parser(String Expr){
        StringBuilder current= new StringBuilder();
        Stack<Character> stack = new Stack<>();

        int priority;
        for (char expression_character: Expr.toCharArray()){
            priority = get_priority(expression_character);

            if (priority == 0) current.append(expression_character);
            else if (priority == 1) stack.push(expression_character);
            else if (priority > 1){
                current.append(' ');
                while(!stack.empty()){
                    if (get_priority(stack.peek()) >= priority ) current.append(stack.pop());
                    else break;
                }
                stack.push(expression_character);
            }
            else if (priority == -1){
                current.append(' ');
                while(get_priority(stack.peek()) != 1) current.append(stack.pop());
                stack.pop();
            }
        }
        while(!stack.empty()) current.append(stack.pop());
        return current.toString();
    }
    private static Integer answer(String rpn){
        StringBuilder operand = new StringBuilder();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < rpn.length(); i++) {
            if (rpn.charAt(i) == ' ') continue;
            if (get_priority(rpn.charAt(i)) == 0) {
                while (rpn.charAt(i) != ' ' && get_priority(rpn.charAt(i)) == 0) {
                    operand.append(rpn.charAt(i++));
                    if (i == rpn.length()) break;
                }
                stack.push(Integer.parseInt(operand.toString()));
                operand = new StringBuilder();
            }
            if (get_priority(rpn.charAt(i)) > 1) {
                Integer a = stack.pop(), b = stack.pop();

                if (rpn.charAt(i) == '+') stack.push(b + a);
                else if (rpn.charAt(i) == '-') stack.push(b - a);
                else if (rpn.charAt(i) == '*') stack.push(b * a);
                else if (rpn.charAt(i) == '/') stack.push(b / a);
            }
        }
        return stack.pop();

    }

    private static int get_priority(char token){
        if (token == '*' || token == '/') return 3;
        else if (token == '+' || token == '-') return 2;
        else if (token == '(') return 1;
        else if (token == ')') return -1;
        else return 0;
    }

    public static String Calculate(String Expression){
        return String.valueOf(answer(expression_parser(Expression)));
    }
    public static void main(String[] args){
       System.out.println( Calculate("222+2*2"));
    }
}

