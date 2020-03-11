import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EqServlet",
            urlPatterns = {"/calc/equation"})
public class EqServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        String equation = req.getReader().readLine();
        String equationFromSession = (String) httpSession.getAttribute("equation");
        if (!Calculator.validation(equation)) {
            resp.sendError(400);
        } else {
            if (equationFromSession != null) {
                resp.setStatus(200);
            } else {
                resp.setStatus(201);
            }
            httpSession.setAttribute("equation", equation);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        HttpSession httpSession = req.getSession(false);

        resp.setStatus(204);
        httpSession.setAttribute("equation", null);
    }
}
