package web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet(
        name = "VarServlet",
        urlPatterns = {"/calc/*"}
)

public class VariablesServlet extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException{
        HttpSession httpSession = req.getSession();
        String reqVal = req.getRequestURI().substring(6);
        PrintWriter printWriter = resp.getWriter();
        String readLine = req.getReader().readLine();
        if (goodFormat(readLine)){
            if (httpSession.getAttribute(reqVal) == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            httpSession.setAttribute(reqVal, readLine);
        } else {
            resp.setStatus(403);
            printWriter.println("");
        }
    }

        private boolean goodFormat(String variable) {
            char c = variable.charAt(0);
            if (Pattern.matches("[a-z]", Character.toString(c)))
                return true;
            try {
                return Integer.parseInt(variable) >= -10000 && Integer.parseInt(variable) <= 10000;
            } catch (Exception e){
                return false;
            }
        }

        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
            HttpSession session = req.getSession();
            session.setAttribute(req.getRequestURI().substring(6), null);
            resp.setStatus(204);
        }
}
