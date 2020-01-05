package com.ifmo.aumetov;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "Calc",
        urlPatterns = "/calc"
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {

        PrintWriter servletResponseWriter = servletResponse.getWriter();
        String equation = servletRequest.getParameter("equation").replaceAll("\\s+", "");
        Map<String, String[]> parameterMap = servletRequest.getParameterMap();

        while (consistOfLetters(equation)) {
            int i = 0;
            while (i < equation.length()) {
                if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                    equation = equation.replace(
                            String.valueOf(equation.charAt(i)),
                            String.valueOf(parameterMap.get(String.valueOf(equation.charAt(i)))[0])
                    );
                }
                i++;
            }
        }

        CalcMethodUtil calcMethodUtil = new CalcMethodUtil();
        List<String> expression = calcMethodUtil.parse(equation);

        servletResponseWriter.println(calc(expression));
    }

    private static Integer calc(List<String> postfix) {
        Stack<Integer> stack = new Stack<Integer>();
        String addition = "+";
        String subtraction = "-";
        String multiplication = "*";
        String division = "/";

        for (String x : postfix) {
            if (addition.equals(x)) {
                stack.push(stack.pop() + stack.pop());
            } else if (subtraction.equals(x)) {
                Integer b = stack.pop();
                Integer a = stack.pop();
                stack.push(a - b);
            } else if (multiplication.equals(x)) {
                stack.push(stack.pop() * stack.pop());
            } else if (division.equals(x)) {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a / b);
            } else {
                stack.push(Integer.valueOf(x));
            }
        }
        return stack.pop();
    }

    private boolean consistOfLetters(String letter) {
        int i = 0;
        while (i < letter.length()) {
            if ('a' < letter.charAt(i) && 'z' > letter.charAt(i)) {
                return true;
            }
            i++;
        }

        return false;
    }
}
