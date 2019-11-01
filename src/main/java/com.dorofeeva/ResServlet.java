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
        HttpSession session = req.getSession();
        try {
            if (session == null)
                throw new ServletException("Session is empty");
            String equation = session.getAttribute("equation").toString();
            if (equation == null)
                throw new ServletException("There is no equation");
            String varList = session.getAttribute("varList").toString();
            if (varList == null)
                throw new ServletException("There are no variables");

            StringBuilder STB = new StringBuilder();
            String val;
            char varname;
//я не умею делать мапы так что только так

            for (int i = 0; i < equation.length(); i++){
                varname = equation.charAt(i);
                if (itsLet(equation.substring(i, i + 1))) {
                    val = session.getAttribute(equation.substring(i, i + 1)).toString();
                    while (itsLet(val)) {
                        val = session.getAttribute(String.valueOf(val)).toString();
                    }
                    STB.append(val);
                }
                else
                    STB.append(varname);
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
    private boolean itsLet(String s){
        boolean f;
        f = s.charAt(0)>='a' && s.charAt(0)<='z';
        return f;
    }
}