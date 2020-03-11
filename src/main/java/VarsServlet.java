import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "VarsServlet",
            urlPatterns = {"/calc/*"})
public class VarsServlet extends HttpServlet {
    private int URL_CONST = 6;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession(false);
        String var = req.getRequestURI().substring(URL_CONST);
        String variableFromBody = req.getReader().readLine();
        if (isValid(variableFromBody)){
            if (httpSession.getAttribute(var) != null) {
                resp.setStatus(200);
            } else {
                resp.setStatus(201);
            }
            httpSession.setAttribute(var, variableFromBody);
        } else {
            resp.sendError(403);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession ses = req.getSession(false);
        ses.setAttribute(req.getRequestURI().substring(URL_CONST), null);
        resp.setStatus(204, "del");
    }

    private boolean isValid(String varToChack) {
        if (varToChack.charAt(0) >= 'a' && varToChack.charAt(0) <= 'z')
            return true;
        try {
            return Integer.parseInt(varToChack) > -10001 && Integer.parseInt(varToChack) < 10001;
        } catch (Exception e){
            return false;
        }
    }
}