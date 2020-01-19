package web;

import java.util.*;
import java.util.regex.Pattern;

class Calculator{
    private static int calculate (int num1, int num2, char op) {
        int number1 = num2;
        int number2 = num1;
        switch (op) {
            case '*':
                return number1 * number2;
            case '/':
                return number1 / number2;
            case '+':
                return number1 + number2;
            case '-':
                return number1 - number2;
            default:
                return 0;
        }
    }

    private Map<String, String> variables;

    Calculator(Map<String, String> variables) {
        this.variables = variables;
    }

    int solve(String equation) {
        Stack<Integer> resultStack = new Stack<>();
        String finalEquation = toNormal(equation);
        for (int i = 0; i < finalEquation.length(); i++){
            char c = finalEquation.charAt(i);
            if (Pattern.matches("[0-9]", Character.toString(c))) {
                int number = Character.getNumericValue(c);
                resultStack.push(number);
            }
            else if (isOperator(Character.toString(c)))
                resultStack.push(calculate(resultStack.pop(), resultStack.pop(), c));
            else if (variables.containsKey(String.valueOf(c)) || (Object) c instanceof Integer)
                resultStack.push(placeVariable(c));
        }
        return resultStack.pop();
    }

    private int placeVariable(char c) {
        String value = variables.get(String.valueOf(c));

        if (Pattern.matches("[a-z]", Character.toString(value.charAt(0)))) {
            return placeVariable(value.charAt(0));
        }
        return Integer.parseInt(value);
    }

    private static String buildRPN(String equation) {
        StringBuilder op = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for( int i = 0; i < equation.length(); i ++) {
            char c = equation.charAt(i);
            if (isOperand(Character.toString(c)))
                op.append(c);
            else if (c == '(')
                stack.push(c);
            else if (isOperator(Character.toString(c))) {
                if (!stack.isEmpty() && higherPriority(stack.peek(), c))
                    op.append(stack.pop());
                stack.push(c);
            } else if (c == ')') {
                while (stack.peek() != '(')
                    op.append(stack.pop());
                stack.pop();
            }
        }
        while (!stack.isEmpty())
            op.append(stack.pop());
        return op.toString();
    }

    private static String toNormal(String equation) {
        String normEquation = equation.replace(" ", "");
        return buildRPN(normEquation);
    }

    private static boolean isOperator(String c) {
        return  "-".equals(c) || "+".equals(c) || "*".equals(c) || "/".equals(c);
    }

    private static boolean isOperand(String c) {
        return !(isOperator(c)) && !("(".equals(c)) && !(")".equals(c));
    }

    private static boolean higherPriority(char op1, char op2) {
        return (op1 == '/' || op1 == '*') || ((op2 == '+' || op1 == '-') && (op1 == '+' || op1 == '-'));

    }
}
