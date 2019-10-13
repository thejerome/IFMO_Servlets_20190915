package com.leomissing.web.servlets;



import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Stack;


@WebServlet(
        name = "Calc",
        urlPatterns = {"/calc"}
)
public class Calc extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String qtn = req.getParameter("equation");
        Map<String, String[]> vars = req.getParameterMap();
        for (String key:vars.keySet()
        ) {
            String var = vars.get(key)[0];
            if (!var.equals(qtn)) {
                if (1 == var.length() && (var.charAt(0) >= 'a' && var.charAt(0) <= 'z')){
                    var = vars.get(var)[0];
                }
                qtn=qtn.replace(key, var);
            }
        }
        out.println(compute(qtn));
        out.flush();
        out.close();
    }
    private int compute(String qtn){
        Stack<Character> consumers = new Stack<Character>();
        Stack<Integer> valve = new Stack<Integer>();
        char[] moneys = qtn.toCharArray();
        System.out.println(moneys);
        for (int i = 0; i < moneys.length ; i++) {
            if (moneys[i] == ' ') {
                continue;
            }
            if (moneys[i] >= '0' && moneys[i] <= '9'){
                StringBuffer puff = new StringBuffer();
                while (i < moneys.length && moneys[i] >= '0' && moneys[i] <= '9') {
                    puff.append(moneys[i++]);
                }
                i--;
                valve.push(Integer.parseInt(puff.toString()));
            } else if (moneys[i] == '('){
                consumers.push(moneys[i]);
            } else if (moneys[i] == ')'){
                while (consumers.peek() != '('){
                    valve.push(answer(valve.pop(), valve.pop(), consumers.pop()));
                }
                consumers.pop();
            } else {
                while(!consumers.empty() && priorty(moneys[i], consumers.peek())){
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
    private static boolean priorty(char coin1, char coin2){
        if ((coin2 == '(' || coin2 == ')') ||  ((coin1 == '*' || coin1 == '/') && (coin2 == '+' || coin2 == '-'))){
            return false;
        } else {
            return true;
        }
    }
    private static int answer(int biba, int pipa, char coin){
        if (coin == '+'){
            return biba+pipa;
        }
        if (coin == '-'){
            return pipa-biba;
        }
        if (coin == '*'){
            return biba*pipa;
        }
        if (coin == '/'){
            return pipa/biba;
        }
        return 0;
    }
}