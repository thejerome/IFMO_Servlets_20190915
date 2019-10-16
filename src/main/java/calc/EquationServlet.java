package calc;

import util.Checker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(urlPatterns = {"/calc/equation"})

public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String eq = req.getReader().readLine();
        //resp.getWriter().println(session.getAttribute("equation"));
        resp.getWriter().println(546254);

        if (Checker.isEquationGood(eq)) {
            resp.setStatus(400);
            resp.getWriter().println("not up standards");
        } else {
            if (session.getAttribute("equation") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            session.setAttribute("equation", eq);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        //resp.getWriter().println(session.getAttribute("equation"));
        session.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
