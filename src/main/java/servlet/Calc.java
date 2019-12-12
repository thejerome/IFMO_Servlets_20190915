package servlet;

import java.util.*;
import java.util.regex.Pattern;

class Calc {
    static String replaceVars(Map<String, String> vars) {
        String equation = vars.remove("expr");
        for (Map.Entry<String, String> param : vars.entrySet()) {
            String value;
            if(Character.isAlphabetic(param.getValue().charAt(0))) { // if variable refers to another variable
                value = vars.get(param.getValue()); // get value of referral variable
            } else {
                value = param.getValue();
            }
            equation = equation.replace(param.getKey(), value);
        }
        System.out.printf("[+] Replaced vars with their values. {%s}%n", equation);
        return equation;
    }

    static int evaluate(String equation) {
        List<String> rpn = getPostfixOf(equation); //infix form to postfix form for calc algorithm
        return getValueOf(rpn);
    }

    private static int getValueOf(List<String> postfix) {
        Stack<Integer> digs = new Stack<>();
        Pattern isNum = Pattern.compile("^-?\\d+");
        for (String token :
                postfix) {
            if (isNum.matcher(token).matches()) {
                digs.push(Integer.parseInt(token));
            } else {
                int ans = doOperation(digs.pop(), digs.pop(), token.charAt(0));
                digs.push(ans);
            }
        }
        System.out.printf("[+] Calculated an expression. {%d}%n", digs.peek());
        return digs.pop();
    }

    private static int doOperation(int rightValue, int leftValue, char operation) {
        switch (operation) {
            case '+':
                return leftValue + rightValue;
            case '-':
                return leftValue - rightValue;
            case '*':
                return leftValue * rightValue;
            case '/':
                return leftValue / rightValue;
            default:
                return 0;
        }
    }

    private static List<String> getPostfixOf(String input) {
        StringTokenizer stk = new StringTokenizer(input, "(+-*/) ", true); // tokenizer
        List<String> output = new ArrayList<>(); // list of strings for postfix output
        Stack<String> ops = new Stack<>(); // operators stack
        Pattern isNum = Pattern.compile("\\d+"); // regex matcher for number detecting
        boolean negative = false;
        while (stk.hasMoreTokens()) {
            String token = stk.nextToken();
            if (isNum.matcher(token).matches()) {
                output.add((negative ? "-":"") + token);
                negative = false;
            } else {
                if ("(".equals(token)) {
                    ops.push(token);
                } else if (")".equals(token)) {
                    while (!ops.isEmpty() && !"(".equals(ops.peek())) {
                        output.add(ops.pop());
                    }
                    ops.pop();
                } else if (!token.isBlank()){
                    if("-".equals(token) &&
                            (output.isEmpty() || !isNum.matcher(output.get(output.size()-1)).matches()) &&
                            (ops.isEmpty() || "(".equals(ops.peek()))) {
                        negative = true;
                    } else {
                        while (!ops.isEmpty() && hasHighPrecedence(token, ops.peek())) {
                            output.add(ops.pop());
                        }
                        ops.push(token);
                    }
                }
            }
        }
        while (!ops.isEmpty()) {
            output.add(ops.pop());
        }
        System.out.printf("[+] Converted infix to postfix. {%s}%n", output);
        return output;
    }

    private static boolean hasHighPrecedence(String firstOperator, String secondOperator) {
        return priorityOf(firstOperator.charAt(0)) <= priorityOf(secondOperator.charAt(0));
    }

    private static int priorityOf(char operator) {
        switch (operator) {
            case '(':
            case ')':
                return 0;
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 3;
        }
    }
}
