package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(name = "servlet.CalcServlet", urlPatterns="/calc")
public class CalcServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        Map<String, String[]> eqVarialbes = new HashMap<String, String[]>(request.getParameterMap());
        String equation = createEquation(eqVarialbes);
        writer.println(calculate(equation));
    }

    private String createEquation(Map<String, String[]> eqVarialbles) {
        String res = eqVarialbles.remove("equation")[0];
        for(Map.Entry<String, String[]> param : eqVarialbles.entrySet()) {
            String value;
            if (Character.isAlphabetic(param.getValue()[0].charAt(0))) {
                value = eqVarialbles.get(param.getValue()[0])[0];
            } else {
                value = param.getValue()[0];
            }
            res = res.replace(param.getKey(), value);
        }
        return res;
    }

    private int calculate(String equation) {
        List<String> res = getPostfixOf(equation);
        return toInt(res);
    }

    private List<String> getPostfixOf(String equation) {
        StringTokenizer stringTokinizer = new StringTokenizer(equation, "(+-*/) ", true);
        List<String> res = new ArrayList<String>();
        Stack<String> operands = new Stack<String>();
        Pattern pattern = Pattern.compile("\\d+");
        while(stringTokinizer.hasMoreTokens()) {
            String token = stringTokinizer.nextToken();
            if (pattern.matcher(token).matches()) {
                res.add(token);
            } else {
                if ("(".equals(token)) {
                    operands.push(token);
                } else if (")".equals(token)) {
                    while(!operands.isEmpty() && !"(".equals(operands.peek())) {
                        res.add(operands.pop());
                    }
                    operands.pop();
                } else if (!" ".equals(token)) {
                    while(!operands.isEmpty() && hasHighPrecendence(token, operands.peek())) {
                        res.add(operands.pop());
                    }
                    operands.push(token);
                }
            }
        }

        while(!operands.isEmpty()) {
            res.add(operands.pop());
        }

        return res;
    }

    private boolean hasHighPrecendence(String first, String second) {
        return priorityOf(first.charAt(0)) <= priorityOf(second.charAt(0));
    }

    private int priorityOf(char operator) {
        switch(operator) {
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

    private int toInt(List<String> postfix) {
        Stack<Integer> digs = new Stack<Integer>();
        Pattern pattern = Pattern.compile("\\d+");
        for(String token: postfix) {
            if (pattern.matcher(token).matches()) {
                digs.push(Integer.parseInt(token));
            } else {
                int ans = doOperation(digs.pop(), digs.pop(), token.charAt(0));
                digs.push(ans);
            }
        }
        return digs.pop();
    }

    private int doOperation(int first, int second, char operation) {
        switch(operation) {
            case '+':
                return first + second;
            case '-':
                return second - first;
            case '*':
                return first * second;
            case '/':
                return second / first;
            default:
                return 0;
        }
    }
}