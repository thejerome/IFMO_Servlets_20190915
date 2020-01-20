package com.apat1n.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Stack;

@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String qtn = req.getParameter("equation");
        Map<String, String[]> vars = req.getParameterMap();
        for (String key:vars.keySet()) {
            String var = vars.get(key)[0];
            if (!var.equals(qtn)) {
                if (1 == var.length() && (var.charAt(0) >= 'a' && var.charAt(0) <= 'z')){
                    var = vars.get(var)[0];
                }
                qtn=qtn.replace(key, var);
            }
        }
        out.println(calculate(qtn));
        out.flush();
        out.close();
    }
    private int calculate(String qtn){
        Stack<Integer> valve = new Stack<>();
        Stack<Character> consumers = new Stack<>();
        char[] moneys = qtn.toCharArray();
        System.out.println(moneys);
        for (int i = 0; i < moneys.length ; i++) {
            if (moneys[i] == ' ') {
                continue;
            }
            if (moneys[i] >= '0' && moneys[i] <= '9'){
                StringBuilder puff = new StringBuilder();
                while (i < moneys.length && moneys[i] >= '0' && moneys[i] <= '9') {
                    puff.append(moneys[i++]);
                }
                i--;
                valve.push(Integer.parseInt(puff.toString()));
            }
            else if (moneys[i] == '('){
                consumers.push(moneys[i]);
            }
            else if (moneys[i] == ')'){
                while (consumers.peek() != '('){
                    valve.push(answer(valve.pop(), valve.pop(), consumers.pop()));
                }
                consumers.pop();
            }
            else {
                while(!consumers.empty() && priority(moneys[i], consumers.peek())){
                    valve.push(answer(valve.pop(), valve.pop(), consumers.pop()));
                }
                consumers.push(moneys[i]);
            }
        }
        while(!consumers.empty()){
            valve.push(answer(valve.pop(), valve.pop(), consumers.pop()));
        }
        return valve.pop();
    }
    private static boolean priority(char op1, char op2){
        return ((op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-')) && (op2 != '(' && op2 != ')');
    }
    private static int answer(int a, int b, char op){
        if (op == '+') {
            return a + b;
        }
        else if (op == '-') {
            return a - b;
        }
        else if (op == '/') {
            return a / b;
        }
        else if (op == '*') {
            return a * b;
        }
        return 0;
    }
}

