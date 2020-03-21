package com.misha.task1;

import java.util.Stack;
import java.util.StringTokenizer;

public class MyCalculator {

    public static MyCalculator instance() { return new MyCalculator(); }

    public int calculateEquation(String equation) {
        Stack<Character> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(equation, "(+-*/)", true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.matches("\\d+")) {
                operands.push(Integer.parseInt(token));
            } else if (token.matches("[+-/*]")) {
                if (operators.isEmpty()) {
                    operators.push(token.charAt(0));
                } else if (prec(token.charAt(0)) > prec(operators.peek())) {
                    operators.push(token.charAt(0));
                } else {
                    operands.push(calculateElement(operands.pop(), operands.pop(), operators.pop()));
                    operators.push(token.charAt(0));
                }
            } else if ("(".equals(token)) {
                operators.push(token.charAt(0));
            } else if (")".equals(token)) {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    operands.push(calculateElement(operands.pop(), operands.pop(), operators.pop()));
                }
                operators.pop();
            } else {
                operands.push(calculateElement(operands.pop(), operands.pop(), operators.pop()));
            }
        }
        while (!operators.isEmpty()) {
            operands.push(calculateElement(operands.pop(), operands.pop(), operators.pop()));
        }
        return operands.pop();
    }

    private int prec(char operator) {
        switch (operator) {
            case '(':
            case ')':
                return 0;
            case '+':
            case '-':
                return 1;
            default:
                return 2;
        }
    }

    private int calculateElement(Integer pop, Integer pop1, Character pop2) {
        if (pop2 == '+') {
            return pop1 + pop;
        }
        if (pop2 == '-') {
            return pop1 - pop;
        }
        if (pop2 == '*') {
            return pop1 * pop;
        }
        if (pop2 == '/') {
            return pop1 / pop;
        }
        return 0;
    }
}
