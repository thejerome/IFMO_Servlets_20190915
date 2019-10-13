package org.mrkaschenko.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mrkaschenko.helpers.Helper;


public class ResultServlet extends HttpServlet {

    public static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<Character>();
        StringBuilder postfix = new StringBuilder(infix.length());
        char c;

        for (int i = 0; i < infix.length(); i++) {
            c = infix.charAt(i);

            if (Helper.isOperand(c)) {
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
                } else if (Helper.isOperator(c)) {
                    if (!stack.isEmpty() &&
                        Helper.getPrecedence(c) <= Helper.getPrecedence(stack.peek())) {
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

    private static int performOperation(int leftOperand,
                                        int rightOperand,
                                        String operator) {
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
            if (Helper.isInteger(a)) {
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

    private static String getNumericEquation(String equation,
                                             Map<String, String> map) {
        StringBuilder numEquation = new StringBuilder();
        String parameter = "";

        for(int i = 0; i < equation.length(); ++i) {
            if(equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                if(map.containsKey(Character.toString(equation.charAt(i)))) {
                    parameter = map.get(Character.toString(equation.charAt(i)));
                } else {
                    return null;
                }
                while(parameter.charAt(0) >= 'a' && parameter.charAt(0) <= 'z') {
                    if(map.containsKey(Character.toString(parameter.charAt(0)))) {
                        parameter = map.get(Character.toString(parameter.charAt(0)));
                    }
                    else {
                        return null;
                    }
                }
                numEquation.append(parameter);
            } else {
                numEquation.append(equation.charAt(i));
            }
        }
        System.out.println(numEquation.toString());
        return numEquation.toString();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Map<String, String> map = new HashMap<String, String>();

        HttpSession session = request.getSession(false);
        Enumeration e = session.getAttributeNames();

        if(e.hasMoreElements()) {
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, (String) session.getAttribute(name));
            }
        } else {
            response.sendError(409, "no values");
	    return;
        }

        String equation = map.get("equation");

        String postfixEquation = delimitString(infixToPostfix(equation));
        String numEquation = getNumericEquation(postfixEquation, map);
        if (numEquation == null) {
            response.sendError(409, "lack of data");
	    return;
        }
        int result = evaluate(numEquation);
        out.print(result);
    }
}
