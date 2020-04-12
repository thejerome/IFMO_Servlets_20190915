package com.slavalapin.ifmo.web.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
        name = "StringCalculator",
        urlPatterns = "/calc"
)

public class StringCalculator extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest requestCalculation, HttpServletResponse responseCalculation)
            throws ServletException, IOException {

        final Map<String, String[]> requestParameters = requestCalculation.getParameterMap();
        String expressionOriginal = requestParameters.get("equation")[0];

        ArrayDeque<String> expressionReady = mapVariables(toPolishNotation(expressionOriginal), requestParameters);
        int result = compute(expressionReady);

        //StringBuilder result = new StringBuilder();
        /*while (!expressionReady.isEmpty()) {
            result.append(expressionReady.poll());
            result.append(" ");
        }*/


        PrintWriter responseBodyWriter = responseCalculation.getWriter();
        responseBodyWriter.print(result/*.toString()*/);
        responseBodyWriter.flush();
        responseBodyWriter.close();
    }


    private int compute(ArrayDeque<String> expression) {
       Stack<Integer> calculationStack = new Stack<>();
       while (!expression.isEmpty()) {
           String step = expression.poll();
           if(Character.isDigit(step.charAt(0)))
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
                       throw new IllegalStateException("Unexpected value: " + step);
               }
               calculationStack.push(resultIntermediate);
           }
       }
       return calculationStack.pop();
    }

    private ArrayDeque<String> toPolishNotation(String inputExpression) {

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

    private ArrayDeque<String> mapVariables (ArrayDeque<String> templateExpression, Map<String, String[]> variables) {
        ArrayDeque<String> builtExpression = new ArrayDeque<>();
        for (String item : templateExpression){
            if (Character.isLetter(item.charAt(0)))
                builtExpression.offer(untangleVariablesMap(variables, item));
            else
                builtExpression.offer(item);
        }
        return builtExpression;
    }

    private String untangleVariablesMap(Map<String, String[]> variables, String variableName) {
        String value = variables.get(variableName)[0];
        if (Character.isDigit(value.charAt(0)))
                return value;
        else
            return untangleVariablesMap(variables, value);
    }
}

