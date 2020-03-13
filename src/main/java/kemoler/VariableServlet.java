package kemoler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "VariableServlet", urlPatterns = {"/calc/*"})
public class VariableServlet extends HttpServlet {

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        String var = req.getRequestURI().substring(6);
        String str = req.getReader().readLine();
        if (goodFormat(str)) {
            if (httpSession.getAttribute(var) == null) {
                resp.sendError(201);
                System.out.println("var" + var + "created");
            } else {
                resp.sendError(200);
                System.out.println("var" + var + "updated");
            }
            httpSession.setAttribute(var, str);
            return;
        }
        resp.sendError(403);
    }

    private boolean goodFormat(String str) {
        if (str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
            return true;
        return Calculator.isDigit(str) && Integer.parseInt(str) >= -10000 && Integer.parseInt(str) <= 10000;
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        String var = req.getRequestURI().substring(6);

        resp.setStatus(204, "deleted");
        session.setAttribute(var, null);
    }
}
