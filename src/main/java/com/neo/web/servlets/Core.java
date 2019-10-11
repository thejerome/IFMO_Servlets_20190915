package com.neo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

@WebServlet(
        name = "Core",
        urlPatterns = "/calc"
)

public class Core extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        final Map<String, String[]> parameterMap= req.getParameterMap();

        String equation = req.getParameter("equation").replace(" ", "");

        while (!Pattern.matches("^[0-9*+-/()]+$", equation))
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet())
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
        ArrayList test = new ArrayList();
        test = inputCleaner(equation);

        try {
            out.println(infixCalculator(test));
        } catch (SyntaxErrorException e) {
            e.printStackTrace();
        }


        out.flush();
        out.close();

    }

    private static Stack<Integer> operandStack = new Stack<Integer>();

    private static Stack<String> operatorStack = new Stack<String>();

    private static final String OPERATORS = "+-/*()[]{}";
    private static final String BRACES = "()[]{}";
    private static final String NONBRACES = "+-/*";
    //                                       +  -  /  *   (   )   [   ]   {   }
    private static final int[] PRECEDENCE = {1, 1, 2, 2, -1, -1, -1, -1, -1, -1};
    static ArrayList<String> input = new ArrayList<String>();

    public static ArrayList inputCleaner(String postfix){
        StringBuilder sb = new StringBuilder();
        String noSpaces = postfix.replace(" ", "");
        try {
            for (int i = 0; i < noSpaces.length(); i++) {
                char c = noSpaces.charAt(i);
                boolean isNum = (c >= '0' && c <= '9');

                if (isNum) {
                    sb.append(c);
                    if (i == noSpaces.length()-1) {
                        input.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                } else if (c == '.') {
                    for (int j = 0; j < sb.length(); j++) {
                        if (sb.charAt(j) == '.') {
                            throw new SyntaxErrorException("");
                        } else if (j == sb.length() - 1) {
                            sb.append(c);
                            j = (sb.length() + 1);
                        }
                    }
                    if (sb.length() == 0) {
                        sb.append(c);
                    }
                    if (i == noSpaces.length()-1) {
                        throw new SyntaxErrorException("");
                    }
                } else if (OPERATORS.indexOf(c)!= -1) {
                    if (sb.length() != 0) {
                        input.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    sb.append(c);
                    input.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    throw new SyntaxErrorException("");
                }
            }

            int numLP = 0;
            int numRP = 0;
            int numLB = 0;
            int numRB = 0;
            int numLBr = 0;
            int numRBr = 0;

            for (int f = 0; f < input.size(); f++) {
                switch (input.get(f)) {
                    case "(": numLP++;
                        break;
                    case "[": numLB++;
                        break;
                    case "{": numLBr++;
                        break;
                    case ")": numRP++;
                        break;
                    case "]": numRB++;
                        break;
                    case "}": numRBr++;
                        break;
                    default: //do nothing
                        break;
                }

            }
            if (numLP != numRP || numLB != numRB || numLBr != numRBr) {
                throw new SyntaxErrorException("The number of brackets, braces, or parentheses don't match up!");
            }

            int doop = 0;
            int scoop = 0;
            int foop = 0;
            for (int f = 0; f < input.size(); f++) {
                String awesome = input.get(f);
                switch (awesome) {
                    case "(": doop++;
                        break;
                    case "[": scoop++;
                        break;
                    case "{": foop++;
                        break;
                    case ")": doop--;
                        break;
                    case "]": scoop--;
                        break;
                    case "}": foop--;
                        break;
                    default: //do nothing
                        break;
                }
                if (doop < 0 || scoop < 0 || foop < 0) {
                    throw new SyntaxErrorException("");
                }
            }
            if (NONBRACES.indexOf(input.get(input.size()-1)) != -1) {
                throw new SyntaxErrorException("");
            }
            return input;
        } catch (SyntaxErrorException ex) {
            return input;
        }
    }

    private static void processOperator(String op) throws SyntaxErrorException {
        if (operatorStack.empty() || op.equals("(") || op.equals("[") || op.equals("{")) {
            operatorStack.push(op);
        } else {
            String topOp = operatorStack.peek();
            if (precedence(op) > precedence(topOp)) {
                topOp = op;
                operatorStack.push(op);
            } else {
                while (operandStack.size() >= 2 && !operatorStack.isEmpty()) {
                    int r = operandStack.pop();
                    int l = operandStack.pop();
                    String work = getNextNonBracerOperator();

                    doOperandWork(work, l, r);

                    if(op.equals("(") || op.equals("[") || op.equals("{")) {
                        //matching '(' popped - exit loop.
                        operandStack.push(l);
                        operandStack.push(r);
                        break;
                    }

                    if (!operatorStack.empty()) {
                        //reset topOp
                        topOp = operatorStack.peek();
                    }
                }

                //assert: Operator stack is empty or
                // current operator precedence > top of stack operator precedence.
                if(!op.equals(")") || !op.equals("}") || !op.equals("}")) {
                    operatorStack.push(op);
                }
            }
        }
    }

    public static String infixCalculator(ArrayList<String> expressions) throws SyntaxErrorException {
        for (String expression : expressions) {
            if (OPERATORS.indexOf(expression) == -1) {
                operandStack.push(Integer.parseInt(expression));
            } else {
                processOperator(expression);
            }
        }
        while (operandStack.size() >= 2 && !operatorStack.isEmpty())
        {

            int r = operandStack.pop().intValue();
            int l = operandStack.pop().intValue();
            String work = getNextNonBracerOperator();

            doOperandWork(work, l, r);
        }
        if(operandStack.isEmpty())
            return null;
        return String.valueOf(operandStack.pop());
    }

    private static String getNextNonBracerOperator() {
        String work = "\0"; // \0 is null,
        while(!operatorStack.isEmpty() && NONBRACES.indexOf(work) == -1)
            work = operatorStack.pop();
        return work;
    }

    private static void doOperandWork(String work, int l, int r) throws SyntaxErrorException {
        switch (work) {
            case "+": operandStack.push(l+r);
                break;
            case "-": operandStack.push(l-r);
                break;
            case "*": operandStack.push(l*r);
                break;
            case "/": operandStack.push(l/r);
                break;
            default:
                throw new SyntaxErrorException("Invalid operand " + work);
        }
    }

    private static int precedence(String op) {
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }

    public static class SyntaxErrorException extends Exception {
        SyntaxErrorException(String message) {
            super(message);
        }
    }
}
