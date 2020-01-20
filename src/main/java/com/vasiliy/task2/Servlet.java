package com.vasiliy.task2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;

@WebServlet(name = "CalcServlet", urlPatterns = "/calc/*")
public class Servlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reqPathInfo = req.getPathInfo().substring(1);
        String reqBody = req.getReader().readLine();
        HttpSession reqSession = req.getSession();
        resp.setStatus(reqSession.getAttribute(reqPathInfo) == null ? 201 : 200);
        reqSession.setAttribute(reqPathInfo, reqBody);
        resp.getWriter().print(reqSession.getAttribute(reqPathInfo));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession reqSession = req.getSession();
        Enumeration<String> attributeNames = reqSession.getAttributeNames();
        HashMap<String, String> parameterMap = new HashMap<>();
        while (attributeNames.hasMoreElements()) {
            String nextElement = attributeNames.nextElement();
            parameterMap.put(nextElement, reqSession.getAttribute(nextElement).toString());
        }

        final String[] equation = {parameterMap.remove("equation")};
        parameterMap.forEach((key, value) -> {
            if (Character.isAlphabetic(value.charAt(0))) {
                equation[0] = equation[0].replace(key, parameterMap.get(value));
            }
            equation[0] = equation[0].replace(key, value);
        });

        String eq = equation[0].replace(" ", "");
        Stack<Character> opers = new Stack<>();
        Stack<Integer> nums = new Stack<>();
        int negativeTrigger = 1;
        StringTokenizer stringTokenizer = new StringTokenizer(eq, "+-*/()", true);
        while (stringTokenizer.hasMoreElements()) {
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.matches("[+-/*]")) {
                if (nums.isEmpty()) {
                    negativeTrigger = -1;
                } else if (opers.isEmpty() || isHeStronger(nextToken.charAt(0), opers.peek())) {
                    opers.push(nextToken.charAt(0));
                } else {
                    nums.push(calc(opers.pop(), nums.pop(), nums.pop()));
                    opers.push(nextToken.charAt(0));
                }
            } else if (nextToken.matches("\\d+")) {
                nums.push(Integer.parseInt(nextToken) * negativeTrigger);
                negativeTrigger = 1;
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
        resp.getWriter().print(nums.peek());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute(req.getPathInfo().substring(1));
        resp.setStatus(204);
    }

    private Integer calc(Character operation, Integer num2, Integer num1) {
        return (operation == '+') ? num1 + num2 :
                (operation == '-') ? num1 - num2 :
                        (operation == '*') ? num1 * num2 :
                                (operation == '/') ? num1 / num2 : 0;
    }

    private int getStrength(Character o) {
        return (o == '(' || o == ')') ? 0 : (o == '+' || o == '-') ? 1 : 2;
    }

    private boolean isHeStronger(char o1, Character o2) {
        return getStrength(o1) > getStrength(o2);
    }

}
