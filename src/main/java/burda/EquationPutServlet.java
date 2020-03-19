package burda;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "EquationPutServlet",
        urlPatterns = {"/calc/equation"}
)
public class EquationPutServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();
        String inputData = req.getReader().readLine();

        if (!ExtensionsUtils.containsOperation(inputData) || ExtensionsUtils.containsUnknown(inputData)){
            res.setStatus(400);
            out.println("bad format");
        }
        else {
            res.setStatus(session.getAttribute("equation") == null ? 201 : 200);
            session.setAttribute("equation", inputData);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.setAttribute("equation", null);
        res.setStatus(204);
    }
}