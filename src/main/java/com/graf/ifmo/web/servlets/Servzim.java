package com.graf.ifmo.web.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(
        name = "Servzim",
        urlPatterns = "/calc"
)


public class Servzim extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        final String equation = req.getParameter("equation");
        final Map<String, String[]> parameterMap = req.getParameterMap();

        String normEquation = TruthSeeker(parameterMap);

        //  out.print(parameterMap.get(("equation"))[0] + "  ||  " + normEquation);


        transform(normEquation);
        Queue<String> polishEquation = getNewNotation();

        // out.print("  ||  " + polishEquation);

        int ans = doParse(polishEquation);
        out.print(ans);


        out.flush();
        out.close();
    }


    String TruthSeeker(Map<String, String[]> parameterMap) {

        Map<String, Integer> solved = new HashMap<>();
        String normEquation = "(" + parameterMap.get("equation")[0] + ")";


        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {

            if (!parameterEntry.getKey().contentEquals("equation")) {

                if (Pattern.matches("[-0123456789]*", parameterEntry.getValue()[0])) {
                    solved.put(parameterEntry.getKey(), Integer.parseInt(parameterEntry.getValue()[0]));
                } else {
                    solved.put(parameterEntry.getKey(), Integer.parseInt((parameterMap.get(parameterEntry.getValue()[0])[0])));
                }
                Pattern p = Pattern.compile(parameterEntry.getKey());
                Matcher m = p.matcher(normEquation);
                normEquation = m.replaceAll(solved.get(parameterEntry.getKey()).toString());
            }
        }


        return normEquation.replaceAll(" ", "");
    }

    //VARS:
    Stack<Character> stack = new Stack<>();
    Queue<String> output;

    //преобразует обычную запись в обратную польскую нотацию
    public void transform(String line) {

        output = new ArrayDeque<>();

        int size = line.length();

        int i = 0;
        while (i < size) {
            char el = line.charAt(i);

            switch (el) {
                case '+':
                case '-':
                    gotOper(el, 1);
                    break;
                case '*':
                case '/':
                    gotOper(el, 2);
                    break;
                case '(':
                    stack.push(el);
                    break;
                case ')':
                    gotParen(el);
                    break;
                default:
                    String chislo = "";

                    while (Character.isDigit(line.charAt(i))) {
                        chislo += line.charAt(i);
                        i++;
                    }
                    output.offer(chislo);
                    i--;
                    break;

            }
            i++;
        }

        while (!stack.isEmpty()) output.offer(stack.pop().toString());

    }

    private void gotOper(char currentOp, int priority) {

        while (!stack.isEmpty()) {
            char opTop = stack.pop();
            if (opTop == '(') {
                stack.push(opTop);
                break;
            } else {
                int oldPriority = -1;
                if (opTop == '+' || opTop == '-') oldPriority = 1;
                else oldPriority = 2;

                if (oldPriority < priority) {
                    stack.push(opTop);
                    break;
                } else output.offer("" + opTop);
            }
        }
        stack.push(currentOp);
    }

    private void gotParen(char el1) {

        while (!stack.isEmpty()) {
            char elx = stack.pop();
            if (elx == '(') break;
            else output.offer("" + elx);
        }
    }

    public Queue<String> getNewNotation() {
        return output;
    }


    public int doParse(Queue<String> queue) {

        Stack<Integer> polStack = new Stack<>();
        while (!queue.isEmpty()) {
            String q = queue.peek();
            if (Pattern.matches("^[0-9]+$", q)) {
                polStack.push(Integer.parseInt(q));
            } else {
                char op = queue.peek().charAt(0);
                int b = polStack.pop();
                int a = polStack.pop();
                int res = 0;
                if (op == '+')
                    res = a + b;
                else if (op == '-')
                    res = a - b;
                else if (op == '*')
                    res = a * b;
                else if (op == '/')
                    res = a / b;
                polStack.push(res);
            }
            queue.poll();
        }
        return polStack.pop();
    }

}
