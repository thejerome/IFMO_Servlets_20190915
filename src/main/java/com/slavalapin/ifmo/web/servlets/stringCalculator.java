package com.slavalapin.ifmo.web.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
        name = "StringCalc",
        urlPatterns = "/calc"
)

public class StringCalc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest requestCalculation, HttpServletResponse responseCalculation)
            throws ServletException, IOException {

        final Map<String, String[]> requestParameters = requestCalculation.getParameterMap();
        String expressionOriginal = requestParameters.get("equation")[0];

        ArrayDeque<String> expressionReady = mapVariables(toPolishNotation(expressionOriginal), requestParameters);
        String result = compute(expressionReady);

        PrintWriter responseBodyWriter = responseCalculation.getWriter();
        responseBodyWriter.printLn(result.toString());
        responseBodyWriter.flush();
        responseBodyWriter.close();
    }


    private int compute(ArrayDeque<String> expression) {
       Stack<Integer> calculationStack = new Stack<>;
       while (!expression.isEmpty) {
           step = expression.poll();
           if(Character.isDigit(step.charAt(0)))
               calculationStack.push(Integer.parseInt(step));
           else {
               char symbol = step.at(0);
               int rightOperand = calculationStack.pop();
               int leftOperand = calculationStack.pop();
               int resultIntermediate = 0;
               switch (symbol) {
                   case '+':
                       resultIntermediate = rightOperand + leftOperand;
                       break;
                   case '-':
                       resultIntermediate = rightOperand - leftOperand;
                       break;
                   case '*':
                       resultIntermediate = rightOperand * leftOperand;
                       break;
                   case '/':
                       resultIntermediate = rightOperand / leftOperand;
                       break;
                   default:
                       throw new java.lang.IllegalStateException("Unexpected value: " + step);
               }
               calculationStack.push(resultIntermediate);
           }
       }
       return calculationStack.pop();
    }

    private ArrayDeque<String> toPolishNotation(String inputExpression) {

        Strig expression = "(" + inputExpression + ")";
        ArrayDeque<String> expressionPolished = new ArrayDeque<>();

        Stack<Char> operandCharStack = new Stack<>;
        int i = 0;
        while (i < expression.length()) {
            char cursor = expression.charAt(i);

            if (Character.isDigit(cursor) || Character.isLetter(cursor)) {
                String entity = "";
                while (Character.isDigit(cursor) || Character.isLetter(cursor)) {
                    entity = entity + expression.charAt(i);
                    i++;
                }
                expressionPolished.offer(entity);
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
        ArrayDeque<String> builtExpression = new ArrayDeque<>;
        for (String item : templateExpression){
            if Character.isLetter(item.charAt(0))
                builtExpression.offer(untangleVariablesMap(variables, item));
            else
                builtExpression.offfer(item);
        }
        return builtExpression;
    }

    private String untangleVariablesMap(Map<String, String[]> variables, String variableName) {
        String value = variables.get(variableName)[0];
        if Character.isDigit(value.charAt(0))
                return value;
        else
            return untangleVariablesMap(variables, value);
    }
}

