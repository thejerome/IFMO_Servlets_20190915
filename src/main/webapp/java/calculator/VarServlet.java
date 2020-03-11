package calculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name="VarServlet",
        urlPatterns = {"/calc/*"}
)
public class VarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String value = req.getReader().readLine();
        String var1 = req.getRequestURI().substring(6);
        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }
        if (session.getAttribute("var") == null) {
            Map<String, Object> var = new HashMap<String, Object>();
            var.put(var1, value);
            session.setAttribute("var", var);
            resp.setStatus(201);
        } else {
            Map<String, Object> var = (Map<String, Object>) session.getAttribute("var");
            if (var.containsKey(var1)) {
                var.put(var1, value);
                session.setAttribute("var", var);
                resp.setStatus(200);
            }
            else {
                var.put(var1, value);
                session.setAttribute("var", var);
                resp.setStatus(201);
            }
        }
        resp.getOutputStream().print(var1 + ":" + value);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession ses = req.getSession(false);
        if (ses == null)
            ses = req.getSession();
        Map<String, Object> varsMap = (HashMap<String, Object>) ses.getAttribute("var");
        String var1 = req.getRequestURI().substring(6);
        varsMap.remove(var1);
        resp.setStatus(204);
    }
}
