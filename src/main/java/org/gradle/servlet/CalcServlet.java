package org.gradle.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CalcServlet extends HttpServlet {

    private static boolean isInteger(String stringToCheck) {
      try {
        Integer.parseInt(stringToCheck);
      } catch (NumberFormatException e) {
        return false;
      }
      return true;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    private static int getPrecedence(char ch) {
        switch (ch) {
          case '+':
          case '-':
            return 1;
          case '*':
          case '/':
            return 2;
          default:
            return -1;
        }
    }

    private static boolean isOperand(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static String infixToPostfix(String infix) {
      Stack<Character> stack = new Stack<Character>();
      StringBuilder postfix = new StringBuilder(infix.length());
      char c;

      for (int i = 0; i < infix.length(); i++) {
        c = infix.charAt(i);

        if (isOperand(c)) {
          postfix.append(c);
        } else if (c == '(') {
          stack.push(c);
        } else if (c == ')') {
          while (!stack.isEmpty() && stack.peek() != '(') {
            postfix.append(stack.pop());
          }
          if (!stack.isEmpty() && stack.peek() != '(')
            return null;
          else if(!stack.isEmpty())
            stack.pop();
          } else if (isOperator(c)) {
            if (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
              postfix.append(stack.pop());
            }
            stack.push(c);
          }
      }

      while (!stack.isEmpty()) {
        postfix.append(stack.pop());
      }
      return postfix.toString();
    }

    private static String delimitString(String equation) {
      StringJoiner joiner = new StringJoiner(",");
      for (char ch: equation.toCharArray()) {
        joiner.add(Character.toString(ch));
      }
      String delimitedString = joiner.toString();
      return delimitedString;
    }

    private static int performOperation(int leftOperand, int rightOperand, String operator) {
      switch(operator) {
        case "+":
          return leftOperand + rightOperand;
        case "-":
          return leftOperand - rightOperand;
        case "*":
          return leftOperand * rightOperand;
        case "/":
          return leftOperand / rightOperand;
        default:
          return 0;
      }
    }

    private static int evaluate(String equation) {
      String[] arrOfStr = equation.split(",");
      Stack<Integer> stack = new Stack<Integer>();

      for (String a : arrOfStr) {
        if (isInteger(a)) {
          stack.push(Integer.parseInt(a));
        } else {
          int rightOperand = stack.pop();
          int leftOperand = stack.pop();
          int res = performOperation(leftOperand, rightOperand, a);
          stack.push(res);
        }
      }
      int result = stack.pop();
      return result;
    }

    private static String getNumber(char key, Map<String, String> map) {
      String value;
      if (Character.isLetter(key)) {
        value = map.get(Character.toString(key));
        if (isInteger(value)) {
          return value;
        } else {
          value = getNumber(value.charAt(0), map);
          return value;
        }
      }
      else {
        value = map.get(Character.toString(key));
        return value;
      }
    }

    private static String getNumericEquation(String equation, Map<String, String> map) {
      StringBuilder numEquation = new StringBuilder();

      for (char ch: equation.toCharArray()) {
        if (ch == ',') {
          numEquation.append(ch);
        } else if (Character.isLetter(ch)) {
             String value = getNumber(ch, map);
             numEquation.append(value);
        } else {
          numEquation.append(ch);
        }
      }
      String result = numEquation.toString();
      return result;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Enumeration paramNames = request.getParameterNames();
        Map<String, String> map = new HashMap<String, String>();

        while(paramNames.hasMoreElements()) {
           String paramName = (String)paramNames.nextElement();
           //out.print(paramName + ": ");
           String paramValue = request.getParameterValues(paramName)[0];
           map.put(paramName, paramValue);
           //out.print(paramValue + "<br>");
        }

        String equation = map.get("equation");

        String postfixEquation = delimitString(infixToPostfix(equation));

        String numEquation = getNumericEquation(postfixEquation, map);
        int result = evaluate(numEquation);

        out.print(result);
    }
}
