package com.lider.ifmo.web.servlets;

import java.util.Map;
import java.util.Stack;

public class Equation {
    Map<String, String[]> variables;

    public Equation(Map<String, String[]> variables) {
        this.variables = variables;
    }

    public Integer Calculate(String s) {
        s = s.replace(" ", "");

        String RPN = ExpressionToRPN(s);
        System.out.println(RPN);

        Stack<Integer> result = new Stack<Integer>();
        for (int i=0; i < RPN.length(); ++i) {
            if (isVariable(RPN.charAt(i))) {
                result.push(placeVariable(RPN.charAt(i)));
            }
            else if (isOperator(RPN.charAt(i))) {
                result.push( doOperator(result.pop(), result.pop(), RPN.charAt(i)) );
            }
        }
        return result.pop(); //if everything is OK there will be only one element.
    }

    private Integer placeVariable(char c) {
        String value = variables.get(String.valueOf(c))[0];

        if (value.charAt(0) >= 'a' && value.charAt(0) <= 'z') {
            return placeVariable(value.charAt(0));
        }
        return Integer.parseInt(value);
    }

    private int Priority(Character c) {
        if (c.equals('*')) { return 3;}
        if (c.equals('/')) { return 3;}
        if (c.equals('+') || c.equals('-')) { return 2;}
        if (c.equals('(')) { return 1;}
        if (c.equals(')')) { return -1;}
        else return 0; //symbols from 0 to 9 and latin lowercase letters
    }

    private String ExpressionToRPN(String str) {
        StringBuilder current = new StringBuilder();
        Stack<Character> operators = new Stack<Character>();

        for (int i=0; i < str.length(); i++) {
            if (Priority(str.charAt(i)) == 0) { current.append(str.charAt(i)); }
            else if (str.charAt(i) == '(') { operators.push(str.charAt(i)); }

            else if (Priority(str.charAt(i)) >= 2) {
                if (!(operators.isEmpty()) && Priority(operators.peek()) >= Priority(str.charAt(i))) {
                    current.append(operators.pop());
                }
                operators.push(str.charAt(i));
            }

            else if (str.charAt(i) == ')') {
                while (operators.peek() != '(') {
                    current.append(operators.pop());
                }
                operators.pop(); //delete '(' operator from stack
            }
        }

        while(!operators.isEmpty()) {
            current.append(operators.pop());
        }

        return String.valueOf(current);
    }

    private boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/')
            return true;
        return false;
    }
    private boolean isVariable(Object element) {
        return variables.containsKey(String.valueOf(element)) || (element instanceof Integer);
    }

    private Integer doOperator(int right, int left, char operator) {
        if (operator == '+')
            return left + right;
        else if(operator == '-')
            return left - right;
        else if(operator == '*')
            return left * right;
        else if(operator == '/')
            return left / right;
        else
            throw new IllegalArgumentException();
    }

    private String placeValues(String eq, Map<String, String[]> vars) {
        for (int i=0; i < eq.length(); i++) {
            if (eq.charAt(i) >= 'a' && eq.charAt(i) <= 'z') {
                eq = eq.replace(
                        String.valueOf(eq.charAt(i)),
                        vars.get( String.valueOf(eq.charAt(i)) )[0]
                );
            }
        }
        return eq;
    }
}
