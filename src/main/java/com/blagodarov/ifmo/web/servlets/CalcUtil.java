package com.blagodarov.ifmo.web.servlets;

public class CalcUtil {

    public static int eval(String expression) {
        char[] equation = expression.toCharArray();

        // Stack for numbers: 'values' 
        int[] values = new int[26];
        int currentVal = -1;

        // Stack for Operators: 'ops' 
        char[] ops = new char[50];
        int currentOp = -1;
        for (int i = 0; i < equation.length; i++) {

            // If we are on a number
            if (equation[i] >= '0' && equation[i] <= '9') {
                StringBuilder newNumber = new StringBuilder();
                while (i < equation.length && equation[i] >= '0' && equation[i] <= '9') //If number is big
                    newNumber.append(equation[i++]);
                currentVal++;
                values[currentVal] = Integer.parseInt(newNumber.toString());
            }

            //if bracket starts
            if (i < equation.length && equation[i] == '(') {
                currentOp++;
                ops[currentOp] = equation[i];
            }

            //if bracket ends, and also calc
            if (i < equation.length && equation[i] == ')') {
                while (ops[currentOp] != '('){
                    int value2 = values[currentVal];
                    currentVal--;
                    int value1 = values[currentVal];
                    char op = ops[currentOp];
                    currentOp--;
                    values[currentVal] = oneOp(op, value2, value1);
                }
                currentOp--;
            }


            //if operator
            if (i < equation.length && isOp(equation[i])){
                while (currentOp != -1 && importance(equation[i]) <= importance(ops[currentOp])){
                    int value2 = values[currentVal];
                    currentVal--;
                    int value1 = values[currentVal];
                    char op = ops[currentOp];
                    currentOp--;

                    values[currentVal] = oneOp(op, value2, value1);
                }
                currentOp++;
                ops[currentOp] = equation[i];
            }
        }
        //final calculations when no brackets left
        while (currentOp != -1){
            int value2 = values[currentVal];
            currentVal--;
            int value1 = 0;
            if (currentVal == -1)
                currentVal++;
            else
                value1 = values[currentVal];
            char op = ops[currentOp];
            currentOp--;
            values[currentVal] = oneOp(op, value2, value1);
        }
        //the only left is the result 
        return values[currentVal];
    }

    //makes order correct
    public static int importance(char op){
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

    public static int oneOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
        }
        return 0;
    }
}
