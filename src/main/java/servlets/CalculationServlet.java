package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
@WebServlet(name = "CalculationServlet", urlPatterns = {"/calc"})
public class CalculationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        String equation = request.getParameter("equation");
        Map<String, String[]> map = request.getParameterMap();
//        System.out.println(equation);
//        System.out.println(equationToPostfix(equation));
//        System.out.println(mapVariables(equationToPostfix(equation), map));
        out.write(evalPostfix(mapVariables(equationToPostfix(equation), map)));
    }

    private static boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    private static String equationToPostfix(String expr) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i <expr.length() ; i++) {
            char ch = expr.charAt(i);
            if(getPrecedence(ch)>0){
                while(!stack.isEmpty() && (getPrecedence(stack.peek())>=getPrecedence(ch))) {
                    result.append(stack.pop());
                }
                stack.push(ch);
            } else if(ch==')'){
                char x = stack.pop();
                while(x!='('){
                    result.append(x);
                    x = stack.pop();
                }
            }else if(ch=='('){
                stack.push(ch);
            }else{
                result.append(ch);
            }
        }
        for (int i = 0; i <=stack.size() ; i++) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    private static int getPrecedence(char ch){
        switch (ch){
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

    private static int doMath(int l, int r, String op) {
        switch (op) {
            case "+":
                return l + r;
            case "-":
                return l - r;
            case "*":
                return l * r;
            case "/":
                return l / r;
            default:
                return 0;
        }
    }

    private static String mapVariables(String expr, Map<String, String[]> map) {
        StringBuilder numericEquation = new StringBuilder();
        for (int i = 0; i < expr.length(); ++i) {
            char cur = expr.charAt(i);
            if (cur >= 'a' && cur <= 'z') {
                String newVal = map.get(String.valueOf(cur))[0];
                while ( !isNum(newVal) ){
                    newVal =  map.get(newVal)[0];
                }
                numericEquation.append(newVal);
                numericEquation.append("_");
            } else {
                numericEquation.append(expr.charAt(i));
                numericEquation.append("_");
            }
        }
        return numericEquation.toString();
    }

    private static boolean isNum(String s){
        for (int i = 0; i<s.length(); ++i){
            if (s.charAt(i) < '0' || s.charAt(i) > '9'){
                return false;
            }
        }
        return true;
    }

    private static String evalPostfix(String expr)
    {
        Stack<Integer> stack=new Stack<>();
        String[] exprList = expr.split("_");
        System.out.println(Arrays.toString(exprList));
        for (String str : exprList) {
            if (" ".equals(str)) {
                continue;
            } else if (isInteger(str)) {
                stack.push(Integer.parseInt(str));
            } else {
                int rightVal = stack.pop();
                int leftVal = stack.pop();
                stack.push(doMath(leftVal, rightVal, str));
            }
        }
        int res = stack.pop();
        System.out.println("res: " + res);
        return Integer.toString(res);
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(Exception e) {
            return false;
        }
    }


}
