package kemoler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ExpressionServlet", urlPatterns = {"/calc/equation"})
public class ExpressionServlet extends HttpServlet {
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String expression = req.getReader().readLine();
        if (expression.matches("^[A-Za-z ]*$")) {
            resp.sendError(400);
            return;
        }
        if (session.getAttribute("equation") != null) {
            System.out.println("equation " + expression + " updated");
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
            System.out.println("equation " + expression + " created");
        }
        session.setAttribute("equation", expression);
        return;
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setStatus(204);
        HttpSession session = req.getSession(false);
        session.setAttribute("equation", null);
    }
}
