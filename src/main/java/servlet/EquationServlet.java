package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "EquationServlet", urlPatterns = "/calc/equation")
public class EquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession currSession = req.getSession();
        final BufferedReader reader = req.getReader();
        String expression = reader.readLine();
        reader.reset();
        if(currSession.getAttribute("expr") != null) { // update expression in session
            currSession.setAttribute("expr", expression);
            resp.setStatus(200);
            System.out.println("[+] Updated! " + currSession.getAttribute("expr"));
        } else { // create an expression in session if doesn't exist
            currSession.setAttribute("expr", expression);
            resp.setStatus(201);
            System.out.println("[+] Created! " + currSession.getAttribute("expr"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute("expr");
        resp.setStatus(204);
        System.out.println("[+] Deleted!");
    }
}
