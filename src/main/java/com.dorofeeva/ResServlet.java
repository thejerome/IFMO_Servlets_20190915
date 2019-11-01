package com.dorofeeva;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(
        name = "ResServlet",
        urlPatterns = {"/calc/result"}
)

public class ResServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter writer = resp.getWriter();

        try {
            HttpSession session = req.getSession();

            if (session == null)
                throw new ServletException("Session is empty");
            String equation = session.getAttribute("equation").toString();
            if (equation == null)
                throw new ServletException("There is no equation");
            String varList = session.getAttribute("varList").toString();
            if (varList == null)
                throw new ServletException("There are no variables");

            StringBuilder STB = new StringBuilder();
            for (int i = 0; i < equation.length(); i++){
                char var = equation.charAt(i);
                if (Let(equation.substring(i, i + 1))) {
                    String value = session.getAttribute(equation.substring(i, i + 1)).toString();
                    while (Let(value)) {
                        value = session.getAttribute(String.valueOf(value)).toString();
                    }
                    STB.append(value);
                }
                else
                    STB.append(var);
            }
            equation = STB.toString();

            writer.print(CalcP.cal(equation));
            resp.setStatus(200);

        }
        catch (Exception e) {
            resp.setStatus(409);
            writer.print(e.getMessage());

        }
        finally {
            writer.flush();
            writer.close();
        }
    }
    private boolean Let(String s){
        boolean f;
        f = s.charAt(0)>='a' && s.charAt(0)<='z';
        return f;
    }
}