package com.didenko.tasks;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(
        name = "Servlet",
        urlPatterns = {"/calc/result"}
)
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String equation = ((String)session.getAttribute("equation")).replaceAll("\\s", "");
        StringBuilder rpn = rPn(equation);
        ArrayDeque<String> cal = new ArrayDeque<>();
        boolean ok = true;
        try {
            calculation(session, rpn, cal);
        } catch (IllegalStateException e){
            ok = false;
            resp.setStatus(409);
            resp.getWriter().write("...");
        }
        if (ok)
            resp.getWriter().write(cal.getFirst());
    }


    private StringBuilder rPn(String equation){
        StringBuilder sb = new StringBuilder();
        ArrayDeque<String> st = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(equation, "*/+-()", true);
        while (tokenizer.hasMoreTokens()) {
            String t = tokenizer.nextToken();
            if ( (t.charAt(0) >= 'a' && t.charAt(0) <= 'z') ||
                    Character.isDigit(t.charAt(0))) {
                sb.append(t).append('|');
            } else
            if (t.charAt(0) == '+' || t.charAt(0) == '-' || t.charAt(0) == '/' || t.charAt(0) == '*') {
                if (st.peek() != null) {
                    while (pr(st.peek().charAt(0)) >= pr(t.charAt(0))) {
                        String s = st.pop();
                        sb.append(s).append('|');
                        if (st.peek() == null)
                            break;
                    }
                }
                st.push(t);
            } else
            if ("(".equals(t)) {
                st.push(t);
            } else
            if (")".equals(t)) {
                while (!Objects.equals(st.peek(), "(")) {
                    String op = st.pop();
                    sb.append(op).append('|');
                }
                st.pop();
            }
        }
        while (st.peek() != null) {
            sb.append(st.pop()).append('|');
        }
        sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
        return sb;
    }

    private void calculation(HttpSession session,StringBuilder eq, ArrayDeque<String> calc){
        StringTokenizer tokenizer = new StringTokenizer(eq.toString(), "|");
        System.out.println(eq.toString());
        while (tokenizer.hasMoreTokens()) {
            String t = tokenizer.nextToken();
            if (t.charAt(0) == '+' || t.charAt(0) == '-' || t.charAt(0) == '/' || t.charAt(0) == '*') {
                String r = calc.pop();
                String l = calc.pop();
                calc.push(calculate(Integer.parseInt(l), Integer.parseInt(r), t.charAt(0)));
            }
            else if (t.length() == 1 && t.charAt(0) >= 'a' && t.charAt(0) <= 'z') {
                String s = (String) session.getAttribute(t);
                if (s == null){
                    throw new IllegalStateException();
                }
                while (s.charAt(0) >= 'a' && s.charAt(0) <= 'z') {
                    s = (String) session.getAttribute(s);
                    if (s == null){
                        throw new IllegalStateException();
                    }
                }
                calc.push(s);
            } else {
                calc.push(t);
            }
        }
    }

    private int pr(char c) {
        if (c == '*' || c == '/')
            return 2;
        else if (c == '+' || c == '-')
            return 1;
        else
            return 0;
    }

    private String calculate(int lhs, int rhs, char c) {
        switch (c) {
            case '/':
                return String.valueOf(lhs / rhs);
            case '*':
                return String.valueOf(lhs * rhs);
            case '+':
                return String.valueOf(lhs + rhs);
            case '-':
                return String.valueOf(lhs - rhs);
            default:
                return "impossible";
        }
    }

}
