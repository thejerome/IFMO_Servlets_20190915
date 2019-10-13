package com.web.task_second;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.StringTokenizer;

/*
            assertTrue(Utils.findInSource("javax.servlet.Filter"));
            assertTrue(Utils.findInSource("void doFilter"));
            assertTrue(Utils.findInSource("javax.servlet.http.HttpServlet"));
            assertTrue(Utils.findInSource("void doPut"));
            assertTrue(Utils.findInSource("void doDelete"));
            assertTrue(Utils.findInSource("void doGet"));
*/

@WebServlet(
        name = "GetServo",
        urlPatterns = {"/calc/result"}
)
public class GetServo extends HttpServlet {

    boolean alphanummeric(String str) {
        return str.matches("[A-Za-z0-9]+");
    }

    String get_solved_eq(int lpart, int rpart, String str) {
        char c = str.charAt(0);
        int r = 0;
        switch (c) {
            case '/':
                r = lpart / rpart; // ээ почему статик каст не работает
                break;
            case '*':
                r = lpart * rpart;
                break;
            case '-':
                r = lpart - rpart;
                break;
            case '+':
                r = lpart + rpart;
                break;
            default:
                return "";
        }
        return String.valueOf(r);
    }

    boolean propernummer(String str) {
        if (str.charAt(0) == '-' && str.length() == 1)
            return false;
        return str.matches("[-0-9]+");
    }


    boolean propervar(String str) {
        if (str.length() != 1)
            return false;
        return str.matches("[A-Za-z]+");
    }


    String value(HttpServletRequest sreq, String ss) {
        HttpSession session = sreq.getSession(false);
        String s = (String) session.getAttribute(ss); // всё еще нужен статик каст
        if (s == null) {
            throw new IllegalArgumentException();
        }
        while (!propernummer(s)) {
            s = (String) session.getAttribute(s);
            if (s == null) {
                throw new IllegalArgumentException();
            }
        }
        return s;
    }

    boolean istaction(String str) {
        return str.length() == 1 && str.matches("[-*/+]");
    }

    int getweight(String str) {
        char c = str.charAt(0);
        if (c == '-' || c == '+')
            return 1;
        else if (c == '/' || c == '*')
            return 2;
        return 0;
    }

    String invertedpolskanotazi(String eq) {
        StringBuilder str_builder = new StringBuilder();
        Deque<String> act_stack = new LinkedList<>();
        StringTokenizer tokener = new StringTokenizer(eq, "+-*/()", true);
        while (tokener.hasMoreTokens()) {
            String token = tokener.nextToken();
            /**/
            if (alphanummeric(token)) {
                str_builder.append(token);
                str_builder.append(':');

            } else if (token.equals("(")) {
                act_stack.push(token);

            } else if (token.equals(")")) {
                while (!Objects.equals(act_stack.peek(), "(")) {
                    String act = act_stack.pop();
                    str_builder.append(act);
                    str_builder.append(':');
                }
                act_stack.pop();

            } else if (istaction(token)) {
                if (act_stack.peek() != null) {
                    while (getweight(act_stack.peek()) >= getweight(token)) {
                        String act = act_stack.pop();
                        str_builder.append(act);
                        str_builder.append(':');
                        if (act_stack.peek() == null)
                            break;
                    }
                }
                act_stack.push(token);
            }
        }
        /**/
        while (act_stack.peek() != null) {
            String act = act_stack.pop();
            str_builder.append(act);
            str_builder.append(':');
        }
        /*стрипаем*/
        str_builder.setLength(str_builder.length() - 1);
        return str_builder.toString();
    }

    String getresult(HttpServletRequest req, String rev_pol_not) {
        StringTokenizer str_tokener = new StringTokenizer(rev_pol_not, ":");
        Deque<String> calcer = new LinkedList<>();
        while (str_tokener.hasMoreTokens()) {
            String t = str_tokener.nextToken();
            if (propernummer(t)) {
                calcer.push(t);
            } else if (propervar(t)) {
                String valueOfVar;
                valueOfVar = value(req, t);
                calcer.push(valueOfVar);
            } else if (istaction(t)) {
                String rpart = calcer.pop();
                String lpart = calcer.pop();
                calcer.push(get_solved_eq(Integer.parseInt(lpart), Integer.parseInt(rpart), t));
            }
        }
        return calcer.getFirst();
    }


    @Override
    public void doGet(HttpServletRequest sreq, HttpServletResponse sresp) throws IOException {
        HttpSession s = sreq.getSession(false);
        PrintWriter w = sresp.getWriter();
        String res = "";
        if (s == null) {
            sresp.setStatus(409);
        } else {
            String eq = (String) s.getAttribute("equation");
            if (eq == null) {
                sresp.setStatus(409);
            } else {
                eq = eq.replaceAll("\\s", "");
                String rev_pol_not = invertedpolskanotazi(eq);
                try {
                    res = getresult(sreq, rev_pol_not);
                } catch (IllegalArgumentException e) {
                    sresp.setStatus(409);
                }
            }
        }
        if (sresp.getStatus() != 409) {
            sresp.setStatus(200);
            w.write(res);
        } else {
            w.write("Formation error");
        }
        w.flush();
        w.close();
    }


}