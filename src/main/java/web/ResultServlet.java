package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "ResultServlet",
        urlPatterns = {"/calc/result"}
)
public class ResultServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        HttpSession httpSession = request.getSession();
        if(httpSession == null || httpSession.getAttribute("equation") == null) {
            response.sendError(409);
            printWriter.write("");
        } else {
            try {
                String equationRes = String.valueOf(httpSession.getAttribute("equation"));
                Map<String, String> variables = new HashMap<>();
                for (int i=0; i < equationRes.length(); ++i) {
                    if (Pattern.matches("[a-z]", Character.toString(equationRes.charAt(i)))) {
                        String variablesEq = String.valueOf(equationRes.charAt(i));
                        String upVariables = String.valueOf(httpSession.getAttribute(variablesEq));
                        variables.put(variablesEq, upVariables);
                    }
                }
                Calculator equation = new Calculator(variables);
                printWriter.print(equation.solve(equationRes));
            } catch (NullPointerException e) {
                response.setStatus(409);
            }
        }
    }

}
