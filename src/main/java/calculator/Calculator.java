package calculator;


import java.util.Stack;

public class Calculator {
    public static int eval(String equation) {
        char[] tokens = equation.toCharArray();
        Stack<Integer> stValues = new Stack<>();     // Values stack
        Stack<Character> stOps = new Stack<>();      // Operators stack
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sBuilder = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sBuilder.append(tokens[i++]);
                }
                i--;
                stValues.push(Integer.parseInt(sBuilder.toString()));
            } else if (tokens[i] == '(') {
                stOps.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (stOps.peek() != '(') {
                    stValues.push(
                            calc(stValues.pop(), stValues.pop(), stOps.pop())
                    );
                }
                stOps.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!stOps.empty() && hasPrecedence(tokens[i], stOps.peek())) {
                    stValues.push(
                            calc(stValues.pop(), stValues.pop(), stOps.pop())
                    );
                }
                stOps.push(tokens[i]);
            }
        }
        while (!stOps.empty()) {
            stValues.push(
                    calc(stValues.pop(), stValues.pop(), stOps.pop())
            );
        }
        return stValues.pop();
    }

    private static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        return (operator1 != '*' && operator1 != '/') || (operator2 != '+' && operator2 != '-');
    }

    private static int calc(int num1, int num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num2 - num1;
            case '*':
                return num1 * num2;
            case '/':
                return num2 / num1;
            default:
                return 0;
        }
    }
}
