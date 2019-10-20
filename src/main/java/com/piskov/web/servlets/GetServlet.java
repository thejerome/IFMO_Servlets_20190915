package com.piskov.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(
        name = "GetServlet",
        urlPatterns = {"/calc/result"}
)

public class GetServlet extends HttpServlet {

    @Override //
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        PrintWriter writer = resp.getWriter();
        String result = "";
        String expression = map(session);

        if (consistOfLetters(expression)) {
            resp.setStatus(409);
            writer.write("BAD FORMAT!");
        } else {
            result = Service.calculate(expression);
        }
        writer.write(result);
        writer.flush();
        writer.close();
    }


    private static String map(HttpSession session){
        String expression = (String) session.getAttribute("equation");
       for (int i = 0; i<10;i++){
            for (char symbol: expression.toCharArray()) {
                if (symbol >= 'a' && symbol <= 'z') {
                    if  (session.getAttribute(String.valueOf(symbol)) != null)
                        expression = expression.replace(String.valueOf(symbol), String.valueOf(session.getAttribute(String.valueOf(symbol))));
                    else break;
                }
            }
        }
        return expression;
    }

    private static boolean consistOfLetters(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('a' <= expression.charAt(i) && 'z' >= expression.charAt(i)) return true;
        }
        return false;
    }
}