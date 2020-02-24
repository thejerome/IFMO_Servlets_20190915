package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "EquationServlet", urlPatterns = {"/calc/equation"})
public class EquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        String equation = req.getReader().readLine();
        if (!isValid(equation)) {
            resp.setStatus(400);
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
        HttpSession httpSession = req.getSession(false);
        httpSession.setAttribute("equation", null);
    }

    private boolean isValid(String equation) {
        int ind = 0;
        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            if (!Pattern.matches("[A-Zа-яА-Я]", Character.toString(c))) {
                if (isOperator(c)) {
                    ind++;
                }
            } else {
                return false;
            }
        }
        return ind != 0;
    }

    private boolean isOperator(char op){
        return op =='+' || op == '-' || op =='/' || op == '*';
    }
}
