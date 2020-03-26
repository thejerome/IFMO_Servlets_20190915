package calculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/calc/equation"})
public class SimpleEquationServlet extends HttpServlet {
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        String equation = request.getReader().readLine();
        if (equation.matches("^[a-z ]*$")) {
            response.sendError(400);
            return;
        }
        equation = equation.replaceAll(" ", "");
        if (httpSession.getAttribute("equation") != null) {
            response.setStatus(200);
        } else {
            response.setStatus(201);
        }
        httpSession.setAttribute("equation", equation);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)  {
        response.setStatus(204);
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("equation", null);
    }
}
