package com.priko.calc;

import java.util.*;
import java.util.regex.Pattern;


public class ShuntingClass {
    private String eq;
    private Stack<Integer> operatorStack = new Stack<>();
    private StringBuilder output = new StringBuilder();
    private Map<String, String> vars = new HashMap<>();

    /**
     *         Operators
     *  [+] -   (Plus)   - [0]
     *  [-] -  (Minus)   - [1]
     *  [*] - (Multiply) - [2]
     *  [/] -  (Divide)  - [3]
     *
     */

    ShuntingClass() { }
    ShuntingClass(String eq) {
        this.eq = eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getEq() {
        return eq;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public String replaceVars(String token){
        //ArrayList<String> cleanTokenList = new ArrayList<>();
        String currentVar = vars.get(token);
        if(Pattern.matches("[a-z]", currentVar)) {
           currentVar = replaceVars(currentVar);
        }
            return currentVar;
    }

    public String findSolution( String postfixEq ) {
        Deque<String> stack = new ArrayDeque<String>();
        for(String token: postfixEq.split("[ ]")){
            if(Pattern.matches("[0-9]+", token)){
                stack.push(token);
            }else {
                int a = Integer.parseInt(stack.pop());
                int b = Integer.parseInt(stack.pop());
                int res = 0;
                switch (token){
                    case "+":
                        res = b + a;
                        break;
                    case "-":
                        res = b - a;
                        break;
                    case "*":
                        res = b * a;
                        break;
                    case "/":
                        res = b / a;
                        break;
                }
                stack.push(String.valueOf(res));
            }
        }
        return stack.pop();
    }

    String[] parseEq(String infixEq){
        final String numbers = "0123456789";
        final String operators = "+-*/()";
        ArrayList<String> tokenList = new ArrayList<>();
        String[] preparsedEq = infixEq.replaceAll("\\s+", "").split("");
        StringBuilder currentNumber = new StringBuilder();
        for(int i=0; i<preparsedEq.length; i++){
            if((i==0 && preparsedEq[i].equals("-"))
                    || (preparsedEq[i].equals("-") && (operators.contains(preparsedEq[i - 1]) ))){
                currentNumber.append("-");
            }else if (numbers.contains(preparsedEq[i])){
                currentNumber.append(preparsedEq[i]);
            }else if(operators.contains(preparsedEq[i])){
                if(currentNumber.length() > 0 ) {
                    tokenList.add(currentNumber.toString());
                    currentNumber.delete(0, currentNumber.length());
                }
                tokenList.add(preparsedEq[i]);
            }
            else
            if(Pattern.matches("[a-z]", preparsedEq[i])) {
                tokenList.add(replaceVars(preparsedEq[i]));
            }
            if (i == preparsedEq.length - 1 && currentNumber.length() > 0){
                tokenList.add(currentNumber.toString());
                currentNumber.delete(0, currentNumber.length());
            }
        }
        String[] tokens = new String[tokenList.size()];
        tokenList.toArray(tokens);
        return tokens;
    }

    public void convertEq(String infixEq) {
        String operators = "+-*/";
        String[] list = parseEq(infixEq);
        for (String token : list) {
            if (token.isEmpty()) {
                continue;
            }
            char currentToken = token.charAt(0);
            int operatorIndex = operators.indexOf(currentToken);

            // Check if currentToken is an operator
            if (operatorIndex != -1) {
                if(operatorStack.isEmpty()){
                    operatorStack.push(operatorIndex);
                }
                else {
                    while (!operatorStack.isEmpty()){
                        int priorityCurrent = operatorIndex/2;
                        int priorityFromStack = operatorStack.peek()/2;
                        if(priorityFromStack > priorityCurrent || priorityFromStack == priorityCurrent) {
                            output.append(operators.charAt(operatorStack.pop())).append(' ');
                        }
                        else {

                            break;
                        }
                        //!
                    }
                    operatorStack.push(operatorIndex);
                }
            }
            else if (currentToken == '('){
                operatorStack.push(-2);
            }
            else if(currentToken == ')') {
                while (operatorStack.peek() != -2){
                    output.append(operators.charAt(operatorStack.pop())).append(' ');
                }
                operatorStack.pop();
            }
            else {
                output.append(token).append(' ');
            }
        }
        while (!operatorStack.isEmpty()) {
            output.append(operators.charAt(operatorStack.pop())).append(' ');
        }
        setEq(findSolution(output.toString()));
    }
}