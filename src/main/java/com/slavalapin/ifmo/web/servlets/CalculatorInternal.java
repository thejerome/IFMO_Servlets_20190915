package com.slavalapin.ifmo.web.servlets;

import java.util.*;
import java.util.regex.Pattern;


public class CalculatorInternal {

    public static int doMagic(String expressionOriginal, Map<String, String> variablesMap)
            throws MissingResourceException
    {
        ArrayDeque<String> expressionReady = mapVariables(toPolishNotation(expressionOriginal), variablesMap);
        return compute(expressionReady);
    }

    private static int compute(ArrayDeque<String> expression) {
        Stack<Integer> calculationStack = new Stack<>();
        while (!expression.isEmpty()) {
            String step = expression.poll();
            if(Pattern.matches("-?[0-9]+", step))
                calculationStack.push(Integer.parseInt(step));
            else {
                char symbol = step.charAt(0);
                int rightOperand = calculationStack.pop();
                int leftOperand = calculationStack.pop();
                int resultIntermediate;
                switch (symbol) {
                    case '+':
                        resultIntermediate = rightOperand + leftOperand;
                        break;
                    case '-':
                        resultIntermediate = leftOperand - rightOperand;
                        break;
                    case '*':
                        resultIntermediate = rightOperand * leftOperand;
                        break;
                    case '/':
                        resultIntermediate = leftOperand / rightOperand;
                        break;
                    default:
                        throw new java.lang.IllegalStateException("Unexpected value: " + step);
                }
                calculationStack.push(resultIntermediate);
            }
        }
        return calculationStack.pop();
    }

    private static ArrayDeque<String> toPolishNotation(String inputExpression) {

        String expression = "(" + inputExpression + ")";
        ArrayDeque<String> expressionPolished = new ArrayDeque<>();

        Stack<Character> operandCharStack = new Stack<>();
        int i = 0;
        while (i < expression.length()) {
            char cursor = expression.charAt(i);

            if (Character.isDigit(cursor) || Character.isLetter(cursor)) {
                StringBuilder entity = new StringBuilder();
                while (Character.isDigit(cursor) || Character.isLetter(cursor)) {
                    entity.append(cursor);
                    i++;
                    if (i<expression.length())
                        cursor = expression.charAt(i);
                    else
                        break;
                }
                expressionPolished.offer(entity.toString());
                i--;
            }

            else if (cursor == '(') {
                operandCharStack.push(cursor);
            }

            else if (cursor == ')') {
                while (operandCharStack.peek() != '(')
                    expressionPolished.offer(operandCharStack.pop().toString());
                operandCharStack.pop();
            }

            else if (cursor != ' ') { //operand
                while ( operandCharStack.peek() != '(' &&
                        !( (cursor == '*' || cursor == '/') &&
                                (operandCharStack.peek() == '+' || operandCharStack.peek() == '-')
                        )
                )
                    expressionPolished.offer(operandCharStack.pop().toString());
                operandCharStack.push(cursor);
            }

            i++;
        }
        return expressionPolished;
    }

    private static ArrayDeque<String> mapVariables (ArrayDeque<String> templateExpression, Map<String, String> variables)
            throws MissingResourceException
    {
        ArrayDeque<String> builtExpression = new ArrayDeque<>();
        for (String item : templateExpression){
            if (Character.isLetter(item.charAt(0)))
                builtExpression.offer(untangleVariablesMap(variables, item));
            else
                builtExpression.offer(item);
        }
        return builtExpression;
    }

    private static String untangleVariablesMap(Map<String, String> variables, String variableName)
            throws MissingResourceException {

            String value = variables.get(variableName);
            if (value == null)
                throw new MissingResourceException("Cannot compute. Variables missing.", variableName, variableName);
            else {
                if (Pattern.matches("-?[0-9]+", value))
                    return value;
                else
                    return untangleVariablesMap(variables, value);
            }
    }
}