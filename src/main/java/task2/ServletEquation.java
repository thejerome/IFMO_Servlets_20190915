package task2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "ServletEquation",
        urlPatterns = {"/calc/equation"}
)
public class ServletEquation extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String equation = req.getReader().readLine();
        if (req.getSession().getAttribute("equation") == null) {
            resp.setStatus(201);
            session.setAttribute("equation", equation);
        }
        else {
            resp.setStatus(200);
            session.setAttribute("equation", equation);
        }
        resp.getOutputStream().print(equation);
        resp.getOutputStream().flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        req.getSession(false).setAttribute("equation", null);
        resp.setStatus(204);
    }
}
