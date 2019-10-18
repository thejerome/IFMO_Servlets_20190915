import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
    name = "equationServlet",
    urlPatterns = {"/calc/equation"}
)

public class ServletEquation extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String equation = req.getReader().readLine();
        if (session.getAttribute("equation") != null) {
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
        }
        session.setAttribute("equation", equation);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
