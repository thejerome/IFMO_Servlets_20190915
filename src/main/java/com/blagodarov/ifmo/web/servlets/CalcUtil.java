package com.blagodarov.ifmo.web.servlets;

import java.util.Stack;

public class CalcUtil {

    public static int eval(String expression) {
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
                //check for negative numbers
                if (symbol == '-' && (i==0 || equation[i-1] =='(')){
                    values.push(0);
                }
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
