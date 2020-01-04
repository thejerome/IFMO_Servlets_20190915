package calc;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calc {

    private static final Map<Character, Integer> precedence;

    static {
        precedence = new HashMap<>();
        precedence.put('(', 0);
        precedence.put(')', 0);
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);
    }

    // -a/b/c/d
    public int getResult(String equation) {
        Stack<Character> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(equation, "(+-*/)", true);
        int umin = 1;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (isOperand(token)) {
                System.out.println("Current operand = " + token);
                operands.push(Integer.parseInt(token) * umin);
                umin = 1;
            } else if (isOperator(token)) {
                System.out.println("Current operator = " + token);
                if (operands.isEmpty()) {
                    umin = -1;
                }
                else if (operators.isEmpty()) {
                    operators.push(token.charAt(0));
                } else if (precedenceOf(token.charAt(0)) > precedenceOf(operators.peek())) {
                    operators.push(token.charAt(0));
                } else {
                    int res = compute(operands.pop(), operands.pop(), operators.pop());
                    operands.push(res);
                    operators.push(token.charAt(0));
                }
            } else if ("(".equals(token)) {
                operators.push(token.charAt(0));
            } else if (")".equals(token)) {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    operands.push(compute(operands.pop(), operands.pop(), operators.pop()));
                }
                operators.pop();
            } else {
                operands.push( compute(operands.pop(), operands.pop(), operators.pop()));
            }
        }
        while (!operators.isEmpty()) {
            operands.push(compute(operands.pop(), operands.pop(), operators.pop()));
        }
        return operands.pop();
    }

    public boolean isCorrectEquation(String equation) {
        for (int i = 0; i < equation.length(); i++) {
            char token = equation.charAt(i);
            if ("-+*/()".contains(String.valueOf(token))) {
                return true;
            }
        }
        return false;
    }

    private int compute(Integer operand1, Integer operand2, char operator) {
        int res = 0;
        if (operator == '+') {
            res = operand2 + operand1;
        } else if (operator == '-') {
            res = operand2 - operand1;
        } else if (operator == '*') {
            res = operand2 * operand1;
        } else if (operator == '/') {
            res = operand2 / operand1;
        }
        return res;
    }

    private static boolean isOperand(String str) {
        return str.matches("\\d+");
    }

    private static boolean isOperator(String str) {
        return str.matches("[+-/*]");
    }

    private int precedenceOf(Character op) {
        return precedence.get(op);
    }
}