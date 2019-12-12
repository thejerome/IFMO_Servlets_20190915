package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "VariableServlet", urlPatterns = "/calc/*")
public class VariableServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession currSession = req.getSession();
        final BufferedReader reader = req.getReader();
        String variableName = req.getPathInfo().substring(1);
        String value = reader.readLine();
        if(currSession.getAttribute(variableName)!= null) {
            currSession.setAttribute(variableName, value);
            resp.setStatus(200);
            System.out.printf("[+] Updated! {%s = %s}%n", variableName, value);
        } else {
            currSession.setAttribute(variableName, value);
            resp.setStatus(201);
            System.out.printf("[+] Created! {%s = %s}%n", variableName, value);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String variableName = req.getPathInfo().substring(1);
        req.getSession().removeAttribute(variableName);
        resp.setStatus(204);
    }
}
