package calculator;

import java.util.Map;
import java.util.Stack;

public class SimpleCalculator {
    public static String getResult(String infix, Map<String, String> map) throws IllegalStateException {
        System.out.println(infix);
        Stack<Character> postfixStack = new Stack<>();
        StringBuilder newEquation = new StringBuilder(infix.length());
        for (char symbol: infix.toCharArray()) {
            if (Character.isAlphabetic(symbol) || Character.isDigit(symbol))
                newEquation.append(symbol);
            else if (isOperator(symbol)) {
                if (!postfixStack.isEmpty() && (getPrior(postfixStack.peek()) >= getPrior(symbol)))
                    newEquation.append(postfixStack.pop());
                postfixStack.push(symbol);
            }
            else if (symbol == ')') {
                while (postfixStack.peek() != '(') newEquation.append(postfixStack.pop());
                if ((postfixStack.peek() == '('))
                    postfixStack.pop();
            }
            else if (symbol == '(') postfixStack.push(symbol);
        }
        while (!postfixStack.isEmpty()) newEquation.append(postfixStack.pop());
        String postfix = newEquation.toString();
        System.out.println(postfix);
        Stack<String> resultStack = new Stack<>();
        for (char symbol : postfix.toCharArray()) {
//            System.out.println(symbol);
            if (Character.isAlphabetic(symbol) || isNumber(String.valueOf(symbol))) {
                System.out.println("!!!!!!!!");
                resultStack.push(String.valueOf(symbol));
            }
            else resultStack.push(calc(resultStack.pop(), resultStack.pop(), symbol, map));
        }
        return String.valueOf(resultStack.pop());
    }

    private static String calc(String r, String l, char oper, Map<String, String> map) {
//        System.out.println(oper);
        int left = Integer.parseInt(resolve(l, map));
        int right = Integer.parseInt(resolve(r, map));
        System.out.println("left:" + left + "//right:" + right);
        switch (oper) {
            case '+': return String.valueOf(left + right);
            case '-': return String.valueOf(left - right);
            case '*': return String.valueOf(left * right);
            case '/': return String.valueOf(left/right);
            default: return null;
        }
    }

    private static String resolve(String symbol, Map<String, String> map) throws IllegalStateException {
        char character = symbol.charAt(0);
        try {
            if (isNumber(symbol)) return symbol;
            else if (Character.isAlphabetic(character)) {
                String val = map.get(Character.toString(character));
                if (isNumber(val)) return val;
                return resolve(val, map);
            }
            return map.get(Character.toString(character));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }


    public static boolean isNumber(String i) {
        try {
            Integer.parseInt(i);
        } catch (Exception e) {
            return false;
        } return true;
    }

    private static boolean isOperator(char i) {
        return (i == '+' || i == '-' || i == '*' || i == '/');
    }

    private static int getPrior(char i) {
        if (i == '*' || i == '/')
            return 2;
        else if (i == '+' || i == '-')
            return 1;
        else
            return -1;
    }

}
