package servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(name = "servlet.CalcServlet", urlPatterns = "/calc")
public class CalcServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Map<String, String[]> vars = new HashMap<>(request.getParameterMap());
        String equation = replaceVars(vars);
        out.println(evaluate(equation));
    }

    private String replaceVars(Map<String, String[]> vars) {
        String equation = vars.remove("equation")[0];
        for (Map.Entry<String, String[]> param : vars.entrySet()) {
            String value;
            if(Character.isAlphabetic(param.getValue()[0].charAt(0))) { // if variable refers to another variable
                value = vars.get(param.getValue()[0])[0]; // get value of referral variable
            } else {
                value = param.getValue()[0];
            }
            equation = equation.replace(param.getKey(), value);
        }
        System.out.println("[+] Replaced vars with their values.");
        return equation;
    }

    private int evaluate(String equation) {
        List<String> rpn = getPostfixOf(equation); //infix form to postfix form for calc algorithm
        return getValueOf(rpn);
    }

    private int getValueOf(List<String> postfix) {
        Stack<Integer> digs = new Stack<>();
        Pattern isNum = Pattern.compile("\\d+");
        for (String token :
                postfix) {
            if (isNum.matcher(token).matches()) {
                digs.push(Integer.parseInt(token));
            } else {
                int ans = doOperation(digs.pop(), digs.pop(), token.charAt(0));
                digs.push(ans);
            }
        }
        System.out.println("[+] Calculated an expression.");
        return digs.pop();
    }

    private int doOperation(int rightValue, int leftValue, char operation) {
        switch (operation) {
            case '+':
                return rightValue + leftValue;
            case '-':
                return leftValue - rightValue;
            case '*':
                return rightValue * leftValue;
            case '/':
                return leftValue / rightValue;
            default:
                return 0;
        }
    }

    private List<String> getPostfixOf(String input) {
        StringTokenizer stk = new StringTokenizer(input, "(+-*/) ", true); // tokenizer
        List<String> output = new ArrayList<>(); // list of strings for postfix output
        Stack<String> ops = new Stack<>(); // operators stack
        Pattern isNum = Pattern.compile("\\d+"); // regex matcher for number detecting
        while (stk.hasMoreTokens()) {
            String token = stk.nextToken();
            if (isNum.matcher(token).matches()) {
                output.add(token);
            } else {
                if ("(".equals(token)) {
                    ops.push(token);
                } else if (")".equals(token)) {
                    while (!ops.isEmpty() && !"(".equals(ops.peek())) {
                        output.add(ops.pop());
                    }
                    ops.pop();
                } else if (!token.isBlank()){
                    while (!ops.isEmpty() && hasHighPrecedence(token, ops.peek())) {
                        output.add(ops.pop());
                    }
                    ops.push(token);
                }
            }
        }
        while (!ops.isEmpty()) {
            output.add(ops.pop());
        }
        System.out.println("[+] Converted infix to postfix.");
        return output;
    }

    private boolean hasHighPrecedence(String firstOperator, String secondOperator) {
        return priorityOf(firstOperator.charAt(0)) <= priorityOf(secondOperator.charAt(0));
    }

    private int priorityOf(char operator) {
        switch (operator) {
            case '(':
            case ')':
                return 0;
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 3;
        }
    }
}
