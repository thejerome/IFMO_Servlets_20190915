package task2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "VarServlet",
        urlPatterns = "/calc/*"
)
public class VarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String var = req.getRequestURI().substring(6);
        String value = req.getReader().readLine();
        if (check(value)) {
            if (req.getSession().getAttribute(var) == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            req.getSession(false).setAttribute(var, value);
        } else {
            resp.setStatus(403);
        }
    }

    private boolean check(String str) {
        boolean bool1;
        boolean bool2;
        try {
            bool1 = Integer.parseInt(str) >= -10000;
            bool2 = Integer.parseInt(str) <= 10000;
        } catch (NumberFormatException e) {
            return str.charAt(0) >= 'a' && str.charAt(0) <= 'z';
        }
        return bool1 && bool2;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        String var = req.getRequestURI().substring(6);
        session.setAttribute(var, null);
        resp.setStatus(204);
    }
}
