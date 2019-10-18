import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
    name = "variableServlet",
    urlPatterns = {"/calc/*"}
)


public class ServletVariables extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String variable = String.valueOf(req.getRequestURI().charAt(req.getRequestURI().length() - 1));
        String value = req.getReader().readLine();
        if (isGoodValue(value)){
            if (session.getAttribute(variable) != null){
                resp.setStatus(200);
            } else {
                resp.setStatus(201);
            }
            session.setAttribute(variable, value);
        } else {
            resp.setStatus(403);
            PrintWriter output = resp.getWriter();
            output.write("Try again!");
            output.flush();
            output.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute(String.valueOf(req.getRequestURI().charAt(req.getRequestURI().length() - 1)));
        resp.setStatus(204);
    }

    private boolean isGoodValue(String value){
        return value.matches("[a-z]") || Integer.parseInt(value) >= -10000 && Integer.parseInt(value) <= 10000;
    }
}
