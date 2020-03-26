package calculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(urlPatterns = {"/calc/*"})
public class SimpleVarServlet extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        String urlVariable = Character.toString(request.getRequestURI().charAt(request.getRequestURI().length()-1));
        String value = request.getReader().readLine();
        if (valid(value)) {
            if (httpSession.getAttribute(urlVariable) == null) response.setStatus(201);
            else response.setStatus(200);
            httpSession.setAttribute(urlVariable, value);
            return;
        }
        response.sendError(403);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);
        String urlVariable = Character.toString(request.getRequestURI().charAt(request.getRequestURI().length()-1));
        response.setStatus(204, "");
        httpSession.setAttribute(urlVariable, null);
    }

    private static boolean valid(String i) {
        if (Character.isAlphabetic(i.charAt(0))) return true;
        else return (SimpleCalculator.isNumber(i) && Integer.parseInt(i) >= -10000 && Integer.parseInt(i) <= 10000);
    }
}