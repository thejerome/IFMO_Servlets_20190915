package com.mishep.web.servlets;

import com.mishep.web.utils.GetCalculate;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

@WebServlet(
        name = "GetCalcServlet",
        urlPatterns = {"/calc/result"}
)
public class GetCalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String result = "";
            String tokenStr = GetCalculate.getReversePolishNotation(((String) req.getSession().getAttribute("equation")).replaceAll("\\s", ""));
            try {
                result = getResult(req, tokenStr);
            } catch (IllegalArgumentException e) {
                resp.setStatus(409);
            } finally {
                if (resp.getStatus() != 409) {
                    resp.setStatus(200);
                    (resp.getWriter()).write(result);
                }
            }
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResult(HttpServletRequest req, String tokenStr) {
        StringTokenizer str = new StringTokenizer(tokenStr, ".");
        ArrayDeque<String> calc = new ArrayDeque<>();
        do {
            String tokenizer = str.nextToken();
            boolean numberValue = true;
            if (tokenizer.charAt(0) == '-' && tokenizer.length() == 1)
                numberValue = false;
            for (int i = 0; i < tokenizer.length(); i++) {
                if (i == 0 && tokenizer.charAt(i) == '-')
                    continue;
                if (!Character.isDigit(tokenizer.charAt(i))) {
                    numberValue = false;
                }
            }
            if (numberValue) {
                calc.push(tokenizer);
                continue;
            }
            boolean varValue = true;
            for (int i = 0; i < tokenizer.length(); i++) {
                if (!(tokenizer.charAt(i) >= 'a' && tokenizer.charAt(i) <= 'z') || tokenizer.length() != 1) {
                    varValue = false;
                    break;
                }
            }
            if (varValue) {
                String a = tokenizer;
                while (a.charAt(0) >= 'a' && a.charAt(0) <= 'z'){
                    a = (String) (req.getSession()).getAttribute(a);
                    if (a == null)
                        throw new IllegalArgumentException();
                }
                if (tokenizer.charAt(0) >= 'a' && tokenizer.charAt(0) <= 'z'){
                    calc.push(a);
                } else {
                    calc.push(tokenizer);
                }
                continue;
            }
            if (GetCalculate.isOperator(tokenizer)) {
                String op2 = calc.pop();
                String op1 = calc.pop();
                calc.push(String.valueOf(GetCalculate.calcSimpleEquation(Integer.parseInt(op1), Integer.parseInt(op2), tokenizer)));
            }
        } while (str.hasMoreTokens());
        return calc.pop();
    }
}