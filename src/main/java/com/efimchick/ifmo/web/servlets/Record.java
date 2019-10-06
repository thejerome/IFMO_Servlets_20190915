package com.efimchick.ifmo.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "Record",
        urlPatterns = {"/calc"}
)
public class Record extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String equation = req.getParameter("equation");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < equation.length(); ++i) {
            if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                String parameter = req.getParameter(Character.toString(equation.charAt(i)));
                while (parameter.charAt(0) >= 'a' && parameter.charAt(0) <= 'z') {
                    parameter = req.getParameter(Character.toString(parameter.charAt(0)));
                }
                stringBuilder.append(parameter);
            } else {
                stringBuilder.append(equation.charAt(i));
            }
        }
        equation = stringBuilder.toString();
        PrintWriter writer = resp.getWriter();
        writer.println(calculation(equation));
    }

    int calculation(String st) {
        int lft;
        int rht;
        Stack<Integer> stack = new Stack<>();
        String equation = pol(st);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken().trim();
            if (opr(tmp.charAt(0)) && 1 == tmp.length()) {
                rht = stack.pop();
                lft = stack.pop();
                switch (tmp.charAt(0)) {
                    case '+':
                        lft += rht;
                        break;
                    case '*':
                        lft *= rht;
                        break;

                    case '-':
                        lft -= rht;
                        break;
                    case '/':
                        lft /= rht;
                        break;

                    default:
                        break;
                }
                stack.push(lft);
            } else {
                lft = Integer.parseInt(tmp);
                stack.push(lft);
            }
        }
        return stack.pop();
    }

    String pol(String src) {
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

    boolean opr(char x) {
        return x == '+' || x == '-' || x == '*' || x == '/';
    }


    int priority(char x) {
        if (x == '*' || x == '/')
            return 2;
        return 1;
    }
}
