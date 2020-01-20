package com.vasiliy.task1;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

@WebServlet(name = "Calculator", urlPatterns = "/calc")
public class Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        final String[] equation = {parameterMap.remove("equation")[0]};
        parameterMap.forEach((key, values) -> {
            if (Character.isAlphabetic(values[0].charAt(0))) {
                equation[0] = equation[0].replace(key, parameterMap.get(values[0])[0]);
            }
            equation[0] = equation[0].replace(key, values[0]);
        });

        String eq = equation[0].replace(" ", "");
        Stack<Character> opers = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        StringTokenizer stringTokenizer = new StringTokenizer(eq, "+-*/()", true);
        while (stringTokenizer.hasMoreElements()) {
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.matches("[+-/*]")) {
                if (opers.isEmpty() || isHeStronger(nextToken.charAt(0), opers.peek())) {
                    opers.push(nextToken.charAt(0));
                } else {
                    nums.push(calc(opers.pop(), nums.pop(), nums.pop()));
                    opers.push(nextToken.charAt(0));
                }
            } else if (nextToken.matches("\\d+")) {
                nums.push(Integer.valueOf(nextToken));
            } else {
                if ("(".equals(nextToken)) {
                    opers.push(nextToken.charAt(0));
                } else if (")".equals(nextToken)) {
                    while (!opers.isEmpty() && opers.peek() != '(') {
                        nums.push(calc(opers.pop(), nums.pop(), nums.pop()));
                    }
                    opers.pop();
                } else {
                    nums.push(calc(opers.pop(), nums.pop(), nums.pop()));
                }
            }
        }
        while (!opers.isEmpty()) {
            nums.push(calc(opers.pop(), nums.pop(), nums.pop()));
        }
        response.getWriter().println(nums.peek());
    }

    private boolean isHeStronger(char o1, Character o2) {
        return getStrength(o1) > getStrength(o2);
    }

    private int getStrength(Character o) {
        return (o == '(' || o == ')') ? 0 : (o == '+' || o == '-') ? 1 : 2;
    }

    private Integer calc(Character operation, Integer num2, Integer num1) {
        return (operation == '+') ? num1 + num2 :
                (operation == '-') ? num1 - num2 :
                        (operation == '*') ? num1 * num2 :
                                (operation == '/') ? num1 / num2 : 0;
    }
}
