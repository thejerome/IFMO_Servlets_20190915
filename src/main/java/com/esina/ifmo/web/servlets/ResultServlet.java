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

        if (session.getAttribute("equation") == null) {
            error = true;
            resp.setStatus(409);
            out.print("Equation is missing!");
        }
        else {
            equation = session.getAttribute("equation").toString().replace(" ", "");

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
                if (error)
                    break;

                String key = var.getKey();
                String val = sessionVars.get(key);

                if (val == null) {
                    error = true;
                    resp.setStatus(409);
                    out.print("Value is missing!");
                    break;
                }

                while (!error && !Pattern.matches("^[-0-9]+$", val)) {
                    key = val;
                    val = sessionVars.get(key);

                    if (val == null) {
                        error = true;
                        resp.setStatus(409);
                        out.print("Value is missing!");
                        break;
                    }
                }

                if (!error)
                    var.setValue(val);
            }

            if (!error) {
                // Вставляем значения переменных в выражение
                for (int i = 0; i < equation.length(); i++) {
                    if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z')
                        equation = equation.replace(Character.toString(equation.charAt(i)), eqVars.get(Character.toString(equation.charAt(i))));
                }
            }
        }

        // Вычисляем и выводим результат выражения
        if (!error) {
            out.print(Calculator.calculate(equation));
            resp.setStatus(200);
        }

        out.flush();
        out.close();
    }
}