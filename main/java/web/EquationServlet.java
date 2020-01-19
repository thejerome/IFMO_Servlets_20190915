package web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "EquationServlet",
        urlPatterns = {"/calc/equation"}
)

public class EquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException{
        HttpSession httpSession = req.getSession();
        PrintWriter printWriter = resp.getWriter();
        String equation = req.getReader().readLine();
        if (!goodInput(equation)) {
            resp.setStatus(400);
            printWriter.println("");
        } else {
            if (httpSession.getAttribute("equation") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            httpSession.setAttribute("equation", equation);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setStatus(204);
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("equation", null);
    }

    private boolean goodInput(String equation) {
        int indexZero = 0;
        for (int i = 0; i < equation.length(); ++i) {
            char c = equation.charAt(i);
            if (!Pattern.matches("[A-Z]", Character.toString(c))) {
                if (isOperator(c)) {
                        indexZero++;
                }
            } else {
                return false;
            }
        }
        return indexZero != 0;
    }

    private boolean isOperator(char c){
        return c =='+' || c == '-' || c =='/' || c == '*';
    }
}