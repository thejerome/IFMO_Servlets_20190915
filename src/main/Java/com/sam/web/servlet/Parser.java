package com.sam.web.servlet;

import java.util.Stack;

public class Parser {

    public static String parse (String exp){
        Stack<Character> stack = new Stack<>();
        String curr = "";
        int priority;

        for (int i = 0; i < exp.length(); i++) {
            priority = priority_mthd(exp.charAt(i));

            if (priority == 0)
                curr += exp.charAt(i);

            if (priority == 1)
                stack.push(exp.charAt(i));

            if (priority > 1) {
                curr += ' '; //чтобы символы не слипались
                while (!stack.empty()) {
                    if (priority_mthd(stack.peek()) >= priority)
                        curr += stack.pop();
                    else break;
                }
                stack.push(exp.charAt(i));
            }

            if (priority == -1) {
                curr += ' ';
                while (priority_mthd(stack.peek()) != 1)
                    curr += stack.pop();
                stack.pop();
            }
        }

        while(!stack.empty())
            curr += stack.pop();

        return curr;
    }

    private static int priority_mthd(char token){
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

    public static Integer answer_mthd(String prsexp){
        Stack<Integer> stack = new Stack<>();
        String operand = new String();
        for (int i = 0; i < prsexp.length( ); i++){
            if(prsexp.charAt(i) == ' ')
                continue;
            if(priority_mthd(prsexp.charAt(i)) == 0){
                while(prsexp.charAt(i) != ' ' && priority_mthd(prsexp.charAt(i)) == 0){
                    operand += prsexp.charAt(i++);
                    if (i == prsexp.length())
                        break; //дошли до конца
                }
                stack.push(Integer.parseInt(operand));
                operand = new String();
            }
            if (priority_mthd(prsexp.charAt(i)) > 1){
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