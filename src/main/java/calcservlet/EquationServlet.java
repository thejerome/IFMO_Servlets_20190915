package calcservlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "CalcServlet", urlPatterns = "/calc/equation")
public class EquationServlet extends HttpServlet {

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String qtn;
        BufferedReader reader = req.getReader();
        HttpSession session = req.getSession();
        qtn = reader.readLine();
        if (session.getAttribute("equation") != null) {
            String prevQtn = session.getAttribute("equation").toString();
            if (!qtn.equals(prevQtn)) {
                session.setAttribute("equation",qtn);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            session.setAttribute("equation", qtn);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        if(session != null && (session.getAttribute("equation") != null)) {
            session.removeAttribute("equation");
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
