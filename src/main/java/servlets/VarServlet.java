package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "VarServlet", urlPatterns = {"/calc/*"})
public class VarServlet extends HttpServlet {
    private static final int SUBSTRING_IN_URL = 6;

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        String varName= request.getRequestURI().substring(SUBSTRING_IN_URL);
        String line = request.getReader().readLine();
        if (isValid(line)){
            if (httpSession.getAttribute(varName) == null) {
                response.setStatus(201);
            } else {
                response.setStatus(200);
            }
            httpSession.setAttribute(varName, line);
        } else {
            response.setStatus(403);
        }
    }

    private boolean isValid(String variable) {
        if (variable.charAt(0) >= 'a' && variable.charAt(0) <= 'z')
            return true;
        try {
            return Integer.parseInt(variable) >= -10000 && Integer.parseInt(variable) <= 10000;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        String varName = req.getRequestURI().substring(SUBSTRING_IN_URL);
        session.setAttribute(varName, null);
        resp.setStatus(204, "value was deleted");
    }
}
