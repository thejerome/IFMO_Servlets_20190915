package com.lider.ifmo.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet (
        name = "ResultServlet",
        urlPatterns = {"/calc/result"}
)
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();


        try {
            String equation_str = String.valueOf(session.getAttribute("equation"));

            Map<String, String> variables = new HashMap<String, String>();

            for (int i=0; i < equation_str.length(); ++i) {
                if (equation_str.charAt(i) >= 'a' && equation_str.charAt(i) <= 'z') {
                    String varNameStr = String.valueOf(equation_str.charAt(i));
                    String varValueStr = String.valueOf(session.getAttribute(varNameStr));

                    variables.put(varNameStr, varValueStr);
                }
            }

            Equation equation = new Equation(variables);

            out.print(equation.calculate(equation_str));
            out.flush();
        } catch (NullPointerException e) {
            resp.setStatus(409);
        }

    }
}