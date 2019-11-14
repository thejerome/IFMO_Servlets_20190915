package servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(
        name = "Calculator",
        urlPatterns = {"/calc"}
)
public class Calculator extends HttpServlet {
    Stack<String>  stack = new Stack<>();
    Stack<String>  nstack = new Stack<>();
    int prev = 0;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        Map<String, String[]> allMap = req.getParameterMap();
        int a = 0;char[] modequation;
        while (a!=2) {
            modequation = equation.toCharArray();
            for (int i = 0; i < modequation.length; i++)
                if (Character.isLetter(modequation[i])){
                  equation = equation.replaceAll(String.valueOf(modequation[i]),
                          String.valueOf(allMap.get(String.valueOf(modequation[i]))[0]));
                }
            a++;
        }
        modequation = equation.toCharArray();
        for (int i = 0; i < modequation.length; i++)
            if ('0' <= modequation[i] && modequation[i] <= '9') {
                int j = 0;
                if (!stack.isEmpty() && prev == 1) {
                    if (stack.peek().matches("[-+]?\\d+")) {
                        j = Integer.parseInt(stack.peek().trim());
                        stack.pop();
                        stack.push(j + String.valueOf(modequation[i]));
                        prev = 1;
                    }
                } else {
                    stack.push(String.valueOf(modequation[i]));
                    prev = 1;
                }
            } else {
                if (modequation[i] == '+' || modequation[i] == '-') {
                    if (!nstack.isEmpty()) {
                        if (nstack.peek().equals("+") || nstack.peek().equals("-") ||
                                nstack.peek().equals("*") || nstack.peek().equals("/")) {
                            stack.push(String.valueOf(nstack.peek()));
                            nstack.pop();
                        }
                    }
                    nstack.push(String.valueOf(modequation[i]));
                    prev = 0;
                }
                if (modequation[i] == '*' || modequation[i] == '/') {
                    if (!nstack.isEmpty()) {
                        if (nstack.peek().equals("*") || nstack.peek().equals("/")) {
                            stack.push(String.valueOf(nstack.peek()));
                            nstack.pop();
                        }
                    }
                    nstack.push(String.valueOf(modequation[i]));
                    prev = 0;
                }
                if (modequation[i] == '(') {
                    nstack.push("(");
                    prev = 0;
                }
                if (modequation[i] == ')') {
                    prev = 0;
                    do {
                        stack.push(String.valueOf(nstack.peek()));
                        nstack.pop();
                    } while (!nstack.peek().equals("("));
                    nstack.pop();
                }
            }
        while (!stack.isEmpty()) {
            nstack.push(stack.peek());
            stack.pop();
        }
        int leftoperand = 0;
        while (!nstack.isEmpty()) {
        leftoperand = Integer.parseInt(nstack.peek().trim());
        stack.push(nstack.peek());
        nstack.pop();
        int rightoperand;
        if (!nstack.isEmpty())
            rightoperand = Integer.parseInt(nstack.peek().trim());
        else break;
        stack.push(nstack.peek());
        nstack.pop();
        while (nstack.peek().matches("[-+]?\\d+")) {
            leftoperand = rightoperand;
            rightoperand = Integer.parseInt(nstack.peek().trim());
            stack.push(nstack.peek());
            nstack.pop();
        }
        switch (nstack.peek()) {
            case ("+"):
                leftoperand += rightoperand;
                break;
            case ("-"):
                leftoperand -= rightoperand;
                break;
            case ("*"):
                leftoperand *= rightoperand;
                break;
            case ("/"):
                leftoperand = leftoperand / rightoperand;
                break;
        }
        nstack.pop();
        nstack.push(String.valueOf(leftoperand));
        stack.pop();
        stack.pop();
        while (!stack.isEmpty()) {
            nstack.push(stack.peek());
            stack.pop();
        }
    }
        out.print(leftoperand);
        nstack.clear();
        stack.clear();
        out.flush();
        out.close();
    }
}
