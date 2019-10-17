package lab2;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "VariableCheckServlet",
        urlPatterns = {"/calc/*"}
)
public class VariableCheckServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException{
        HttpSession httpSession = req.getSession();
        String reqURI = req.getRequestURI();
        String reqVal = String.valueOf(reqURI.charAt(reqURI.length() - 1));
        PrintWriter writer = resp.getWriter();
        String variableValue = req.getReader().readLine();
        if (itokay(variableValue)){
            if (httpSession.getAttribute(reqVal) == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            httpSession.setAttribute(reqVal, variableValue);
        } else {
            resp.setStatus(403);
            writer.write("Incorrect value!");
        }
        writer.flush();
        writer.close();
    }

    private boolean itokay (String vari) {
        if (vari.charAt(0)>='a' && vari.charAt(0)<='z') {
            return true;
        }
        return Integer.parseInt(vari) >= -10000 && Integer.parseInt(vari) <= 10000;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(String.valueOf(req.getRequestURI().charAt( req.getRequestURI().length() - 1 )));
        resp.setStatus(204);
    }
}