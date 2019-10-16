import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(
        name = "Equation",
        urlPatterns = {"/calc/equation"}
)
public class Equation extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sesh = req.getSession();
        boolean first = false;

        // equation is null from the start
        if (sesh.getAttribute("equation") == null) {
            first = true;
        }

        if (first) {
            resp.setStatus(201);
        }
        else {
            resp.setStatus(200);
        }
        // set modified equation
        sesh.setAttribute("equation", req.getReader().readLine());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // clear equation field
        HttpSession sesh = req.getSession();
        sesh.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
