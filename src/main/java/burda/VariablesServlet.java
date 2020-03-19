package burda;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "VariableServlet",
        urlPatterns = {"/calc/*"}
)
public class VariablesServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();
        String inputData = req.getReader().readLine();

        if (!ExtensionsUtils.checkVariable(inputData,-10000,10000)) {
            res.setStatus(403);
            out.println("bad format");
        }
        else {
            String variableKey = ExtensionsUtils.getVariableKeyFromURI(req);
            res.setStatus(session.getAttribute(variableKey) == null ? 201 : 200);
            session.setAttribute(variableKey, inputData);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        HttpSession sess = req.getSession();
        sess.setAttribute(ExtensionsUtils.getVariableKeyFromURI(req), null);
        res.setStatus(204);
    }
}