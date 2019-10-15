package com.efimchick.ifmo.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "Record",
        urlPatterns = {"/calc/result"}
)
public class Record extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String equation = (String) session.getAttribute("equation");
        if (equation != null) {
            try {
                resp.getWriter().print(calculation(equation, req));
                resp.setStatus(200);
            } catch (IllegalArgumentException e){
                resp.setStatus(409);
                resp.getWriter().println("lack of data");
            }
        } else {
            resp.setStatus(409);
            resp.getWriter().println("lack of data");
        }
    }

    private String getValue(String c, HttpServletRequest req){
        HttpSession session = req.getSession();
        if (c.charAt(0) >= 'a' && c.charAt(0) <= 'z'){
            String newVal = c;
            while (newVal.charAt(0) >= 'a' && newVal.charAt(0) <= 'z'){
                newVal = (String) session.getAttribute(newVal);
                if (newVal == null)
                    throw new IllegalArgumentException();
            }
            return newVal;
        } else {
            return c;
        }
    }

    private String calculation(String st, HttpServletRequest req) {
        String lft;
        String rht;
        Stack<String> stack = new Stack<>();
        String equation = pol(st);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken().trim();
            if (opr(tmp.charAt(0)) && 1 == tmp.length()) {
                rht = stack.pop();
                lft = stack.pop();
                rht = getValue(rht,req);
                lft = getValue(lft,req);
                int ans;
                switch (tmp.charAt(0)) {
                    case '+':
                        ans = Integer.parseInt(lft) + Integer.parseInt(rht);
                        break;
                    case '*':
                        ans = Integer.parseInt(lft) * Integer.parseInt(rht);
                        break;
                    case '-':
                        ans = Integer.parseInt(lft) - Integer.parseInt(rht);
                        break;
                    case '/':
                        ans = Integer.parseInt(lft) / Integer.parseInt(rht);
                        break;
                    default:
                        ans = -1;
                        break;
                }
                stack.push(String.valueOf(ans));
            } else {
                lft = tmp;
                stack.push(lft);
            }
        }
        return stack.pop();
    }


    private String pol(String src) {
        char tmp;
        char buffer;
        StringBuilder res = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            buffer = src.charAt(i);
            if (opr(buffer)) {
                while (sb.length() > 0) {
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                    if (opr(tmp) && (priority(buffer) <= priority(tmp))) {
                        res.append(" ").append(tmp).append(" ");
                        sb.setLength(sb.length() - 1);
                    } else {
                        res.append(" ");
                        break;
                    }
                }
                res.append(" ");
                sb.append(buffer);
            } else if ('(' == buffer) {
                sb.append(buffer);
            }
            else if (')' == buffer) {
                tmp = sb.substring(sb.length() - 1).charAt(0);
                while ('(' != tmp) {
                    res.append(" ").append(tmp);
                    sb.setLength(sb.length() - 1);
                    tmp = sb.substring(sb.length() - 1).charAt(0);
                }
                sb.setLength(sb.length() - 1);
            } else
                res.append(buffer);
        }
        while (sb.length() > 0) {
            res.append(" ").append(sb.substring(sb.length() - 1));
            sb.setLength(sb.length() - 1);
        }
        return res.toString();
    }

    private boolean opr(char x) {
        return x == '+' || x == '-' || x == '*' || x == '/';
    }


    private int priority(char x) {
        if (x == '*' || x == '/')
            return 2;
        return 1;
    }
}
