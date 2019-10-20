package com.esina.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

// Сервлет для вывода результата в /result, если выражение и переменные корректные

@WebServlet(
        name = "ResultServlet",
        urlPatterns = "/calc/result"
)

public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        boolean error = false;
        String equation = "";

        try {
            equation = session.getAttribute("equation").toString();

            if (equation == null)
                throw new ServletException("Equation is missing!");

            equation = equation.replace(" ", "");


            // Собираем переменные из сесии в мапу sessionVars
            Enumeration<String> attributes = session.getAttributeNames();
            Map<String, String> sessionVars = new HashMap();

            while (attributes.hasMoreElements()) {
                String var = attributes.nextElement();
                sessionVars.put(var, session.getAttribute(var).toString());
            }
            sessionVars.remove("equation");

            // Собираем переменные из выражения в мапу eqVars
            Map<String, String> eqVars = new HashMap();
            for (int i = 0; i < equation.length(); i++) {
                if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z')
                    eqVars.put(Character.toString(equation.charAt(i)), "");
            }


            // Подставляем цифры на место eqVars переменных
            for (Map.Entry<String, String> var : eqVars.entrySet()) {
                String key = var.getKey();
                String val = sessionVars.get(key);

                if (val == null)
                    throw new ServletException("Value is missing!");

                while (!Pattern.matches("^[-0-9]+$", val)) {
                    key = val;
                    val = sessionVars.get(key);

                    if (val == null)
                        throw new ServletException("Value is missing!");
                }

                var.setValue(val);
            }

            // Вставляем значения переменных в выражение
            for (int i = 0; i < equation.length(); i++) {
                if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z')
                    equation = equation.replace(Character.toString(equation.charAt(i)), eqVars.get(Character.toString(equation.charAt(i))));
            }

            // Вычисляем и выводим результат выражения
            out.print(CalculatorUtil.calculate(equation));
            resp.setStatus(200);

        }
        catch (ServletException e) {
            resp.setStatus(409);
            out.print(e.getMessage());
        }
        finally {
            out.flush();
            out.close();
        }
    }
}