package com.lider.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by EE on 2018-11-01.
 */

@WebServlet(
        name = "Equation servlet",
        urlPatterns = {"/calc/equation"}
)
public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        ServletOutputStream out = resp.getOutputStream();
//        out.print("Hello, world");
//        out.flush();
//        out.close();
        HttpSession session = req.getSession();
        String equation_str = req.getReader().readLine();
        System.out.println(equation_str);
        PrintWriter out = resp.getWriter();

        boolean expressionFormatIsOk = false;
        for (int i=0; i < equation_str.length(); i++) {
            char tmp = equation_str.charAt(i);
            if ((tmp == '/') || (tmp == '*') || (tmp == '+') || (tmp == '-') || (tmp == '(') || (tmp == ')'))
                expressionFormatIsOk = true;
        }
        if (!expressionFormatIsOk) {
            resp.setStatus(400);
        }
        if (expressionFormatIsOk) {
            if (session.getAttribute("equation") == null)
                resp.setStatus(201);
            else
                resp.setStatus(200);

            session.setAttribute("equation", equation_str);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        session.removeAttribute("equation");
        resp.setStatus(204);
    }
}