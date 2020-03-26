package task1;

import java.util.ArrayDeque;
import java.util.Deque;

public class Calculator {

    public static int getResult(String equation) {
        if (contains(equation)) {
            throw new IllegalArgumentException(equation);
        }
        return evaluate(equation);
    }

    private static boolean contains(String str) {
        for (char i : str.toCharArray()) {
            if (i >= 'a' && i <= 'z') {
                return true;
            }
        }
        return false;
    }

    private static int evaluate(String equation) {

        Deque<Integer> answers = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();

        for (int i = 0; i < equation.toCharArray().length; i++) {
            if (equation.toCharArray()[i] == ' ') {
                continue;
            }

            if (isNum(equation.toCharArray()[i])) {
                StringBuilder builder = new StringBuilder();
                while (i < equation.toCharArray().length && isNum(equation.toCharArray()[i])) {
                    builder.append(equation.toCharArray()[i++]);
                }
                i--;
                answers.push(Integer.valueOf(builder.toString()));
            } else if (equation.toCharArray()[i] == '(') {
                ops.push(equation.toCharArray()[i]);
            } else if (equation.toCharArray()[i] == ')') {
                while (ops.peek() != '(') {
                    answers.push(performOperation(ops.pop(), answers.pop(), answers.pop()));
                }
                ops.pop();
            } else if (equation.toCharArray()[i] == '+' || equation.toCharArray()[i] == '-' || equation.toCharArray()[i] == '*' || equation.toCharArray()[i] == '/') {
                while (!ops.isEmpty() && presendence(equation.toCharArray()[i], ops.peek())) {
                    answers.push(performOperation(ops.pop(), answers.pop(), answers.pop()));
                }
                ops.push(equation.toCharArray()[i]);
            }
        }

        while (!ops.isEmpty()) {
            answers.push(performOperation(ops.pop(), answers.pop(), answers.pop()));
        }

        return answers.pop();
    }

    private static boolean isNum(char chr) {
        switch (chr) {
            case '0':
            case '9':
                return true;
            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            default:
                return false;
        }
    }

    private static boolean presendence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static int performOperation(char op, int number1, int number2) {

        switch (op) {
            case '+':
                return number2 + number1;
            case '-':
                return number2 - number1;
            case '*':
                return number2 * number1;
            case '/':
                return number2 / number1;
            default:
                throw new IllegalArgumentException("Illegal operator: " + op);
        }

    }
}
