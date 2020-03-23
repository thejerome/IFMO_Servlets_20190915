package calculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name="EquationServlet",
        urlPatterns = {"/calc/equation"}
)
public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getReader().readLine();
        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }
        if (session.getAttribute("equation") == null) {
            session.setAttribute("equation", equation);
            resp.setStatus(201);
        }
        else {
            session.setAttribute("equation", equation);
            resp.setStatus(200);
        }
        resp.getOutputStream().print(equation);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession ses = req.getSession(false);
        if (ses == null)
            ses = req.getSession();
        ses.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
