package calcservlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "SwitcherServlet", urlPatterns = "/calc/*")
public class SwitcherServlet extends HttpServlet {

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String varName = req.getPathInfo().substring(1);
        BufferedReader reader = req.getReader();
        String varValue = reader.readLine();
        HttpSession session = req.getSession();
        if  (Character.isDigit(varValue.charAt(0)) || '-' == varValue.charAt(0)){
            int varInt = Integer.parseInt(varValue);
            if (varInt<-10000 || varInt>10000) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                if (session.getAttribute(varName) == null){
                    session.setAttribute(varName, varValue);
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    session.setAttribute(varName, varValue);
                    resp.setStatus(HttpServletResponse.SC_OK);

                }
            }
        } else {
            session.setAttribute(varName, varValue);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String varName = req.getPathInfo().substring(1);
        if(session != null) {
            if(session.getAttribute(varName) != null) {
                session.removeAttribute(varName);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }
}
