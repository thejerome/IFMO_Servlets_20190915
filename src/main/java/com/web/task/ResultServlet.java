package com.web.task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@WebServlet(
        name = "ResultServlet",
        urlPatterns = {"/calc/result"}
)
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        if (session == null) {
            resp.setStatus(409);
            writer.println("session is null");
        } else {
            String equation = (String) session.getAttribute("equation");
            if (equation == null ) {
                resp.setStatus(409);
                writer.println("equation is null");
            } else {
                if (isAllVarsDefined(equation,session)) {
                    resp.setStatus(200);
                    writer.print(Result(equation.replaceAll("\\s", ""), session));
                }
                else {
                    resp.setStatus(409);
                    writer.println("some var is undefined");
                }
            }
        }
        writer.flush();
        writer.close();
    }

    private boolean isAllVarsDefined(String equation, HttpSession session){
        for (int i = 0; i<equation.length(); ++i){
            char c = equation.charAt(i);
            if (c >= 'a' && c <= 'z'){
                String var = String.valueOf(c);
                if (session.getAttribute(var) == null)
                    return false;
            }
        }
        return true;
    }

    private int Result(String eq, HttpSession session) {
        Map<String, String> attributes = new HashMap<>();
        Enumeration<String> en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            String el = en.nextElement();
            attributes.put(el, session.getAttribute(el).toString());
        }
        Map<Character, Integer> normalMap = new HashMap<>();

        for (Map.Entry<String, String> attributesEntry : attributes.entrySet()) {
            if (!(attributesEntry.getKey().equals("equation"))) {
                String val = attributesEntry.getValue();
                if (isVariable(val.charAt(0))) {
                    for (Map.Entry<String, String> attrEntry : attributes.entrySet()) {
                        if (val.equals(attrEntry.getKey())) {
                            val = attrEntry.getValue();
                        }
                    }
                }
                int valInt = Integer.parseInt(val);
                normalMap.put(attributesEntry.getKey().charAt(0), valInt);
            }
        }

        Stack<Character> stackOperators = new Stack<>();
        Stack<Integer> stackVars = new Stack<>();
        for (int i = 0; i < eq.length(); i++) {
            char eqChar = eq.charAt(i);

            if (isVariable(eqChar))
                stackVars.push(normalMap.get(eqChar));

            else if (isSecondaryOperator(eqChar)) {
                while ((stackVars.size()>1) && (!stackOperators.empty()) && (isSecondaryOperator(stackOperators.peek()) || isPriorityOperator(stackOperators.peek()))) {
                    operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);

            } else if (isPriorityOperator(eqChar)) {
                while ((stackVars.size()>1) && (!stackOperators.empty()) && (isPriorityOperator(stackOperators.peek()))) {
                    operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);

            } else if (eqChar == '(') {
                stackOperators.push(eqChar);

            } else if (eqChar == ')') {
                while ( stackVars.size()>1 && stackOperators.peek() != '(') {

                    operation(stackVars, stackOperators);
                }
                stackOperators.pop();
            } else {
                stackVars.push(Integer.parseInt(String.valueOf(eqChar)));
            }
        }

        while (!stackOperators.isEmpty() && stackVars.size()>1)
            operation(stackVars, stackOperators);

        return stackVars.pop();
    }


    private static boolean isPriorityOperator(char oper) {
        return oper == '*' || oper == '/';
    }

    private static boolean isSecondaryOperator(char oper) {
        return oper == '+' || oper == '-';
    }

    private static boolean isVariable(char oper) {
        return oper >= 'a' && oper <= 'z';
    }

    private static void operation(Stack<Integer> var, Stack<Character> oper) {
        int var1 = var.pop();
        if (!var.empty()) {
            int var2 = var.pop();
            switch (oper.pop()) {
                case '+':
                    var.push(var1 + var2);
                    break;
                case '-':
                    var.push(var2 - var1);
                    break;
                case '*':
                    var.push(var1 * var2);
                    break;
                case '/':
                    var.push(var2 / var1);
                    break;
                default:
                    break;
            }
        }
    }

}