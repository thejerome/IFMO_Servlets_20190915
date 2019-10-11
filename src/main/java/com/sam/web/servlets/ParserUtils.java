package com.sam.web.servlets;

import java.util.Stack;

public class ParserUtils {

    public static String parse (String exp){
        Stack<Character> stack = new Stack<>();
        String curr = "";
        int priority;

        for (int i = 0; i < exp.length(); i++) {
            priority = priorityMthd(exp.charAt(i));

            if (priority == 0)
                curr += exp.charAt(i);

            if (priority == 1)
                stack.push(exp.charAt(i));

            if (priority > 1) {
                curr += ' '; //чтобы символы не слипались
                while (!stack.empty()) {
                    if (priorityMthd(stack.peek()) >= priority)
                        curr += stack.pop();
                    else break;
                }
                stack.push(exp.charAt(i));
            }

            if (priority == -1) {
                curr += ' ';
                while (priorityMthd(stack.peek()) != 1)
                    curr += stack.pop();
                stack.pop();
            }
        }

        while(!stack.empty())
            curr += stack.pop();

        return curr;
    }

    private static int priorityMthd(char token){
        if (token == '*' || token == '/')
            return 3;
        else if (token == '+' || token == '-')
            return 2;
        else if (token == '(')
            return 1;
        else if (token == ')')
            return -1;
        else return 0;
    }

    public static Integer answerMthd(String prsexp){
        Stack<Integer> stack = new Stack<>();
        String operand = "";
        for (int i = 0; i < prsexp.length( ); i++){
            if(prsexp.charAt(i) == ' ')
                continue;
            if(priorityMthd(prsexp.charAt(i)) == 0){
                while(prsexp.charAt(i) != ' ' && priorityMthd(prsexp.charAt(i)) == 0){
                    operand += prsexp.charAt(i++);
                    if (i == prsexp.length())
                        break; //дошли до конца
                }
                stack.push(Integer.parseInt(operand));
                operand ="";
            }
            if (priorityMthd(prsexp.charAt(i)) > 1){
                Integer a = stack.pop(),
                        b = stack.pop();
                if(prsexp.charAt(i) == '+') stack.push(b+a);
                else if(prsexp.charAt(i) == '-') stack.push(b-a);
                else if(prsexp.charAt(i) == '*') stack.push(b*a);
                else if(prsexp.charAt(i) == '/') stack.push(b/a);
            }
        }
        return stack.pop();
    }

}