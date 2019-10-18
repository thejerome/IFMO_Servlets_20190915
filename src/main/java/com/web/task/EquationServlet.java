package com.web.task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (
        name = "EquationServlet",
        urlPatterns = {"/calc/equation"}
)

public class EquationServlet extends HttpServlet {

    private static boolean isOperator (char oper) {
        return oper == '*' || oper == '/' || oper == '+' || oper == '-';
    }
    private static boolean isDelimiter(char oper) { return oper == '(' || oper == ')'; }
    private static boolean isVariable (char oper) {
        return oper >= 'a' && oper <= 'z';
    }
    private static boolean isNumber (char oper) {
        return oper >= '0' && oper <= '9';
    }
    private static boolean correctExpression (String exp) {
        for (int i = 0; i < exp.length(); i++) {
            if (!(isNumber(exp.charAt(i)) || isOperator(exp.charAt(i)) || isVariable(exp.charAt(i)) || isDelimiter(exp.charAt(i)) || (exp.charAt(i) == ' ')) || (i != 0 && isVariable(exp.charAt(i)) && isVariable(exp.charAt(i-1)))) {
                return false;
            }
        }
        return true;
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        String equation = req.getReader().readLine();
        if (!correctExpression(equation)) {
            resp.setStatus(400);
            writer.println("expression isn't correct");
        }
        else if (session.getAttribute("equation") == null) {
            resp.setStatus(201);
            session.setAttribute("equation", equation);
        }
        else {
            resp.setStatus(200);
            session.setAttribute("equation", equation);
        }
        writer.flush();
        writer.close();
    }

    @Override
    protected void doDelete (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute("equation");
        resp.setStatus(204);
    }
}
