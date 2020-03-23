package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(name = "SwitcherServlet", urlPatterns = "/calc/*")
public class SwitcherServlet extends HttpServlet {
    protected void doPut(HttpServletRequest requset, HttpServletResponse response) throws IOException {
        String name = requset.getPathInfo().substring(1);
        BufferedReader reader = requset.getReader();
        String value = reader.readLine();
        HttpSession session = requset.getSession();
        if (Character.isDigit(value.charAt(0)) || '-' == value.charAt(0)) {
            int intValue = Integer.parseInt(value);
            if (intValue < -10000 || intValue > 10000) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                if (session.getAttribute(name) == null) {
                    session.setAttribute(name, value);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    session.setAttribute(name, value);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        } else {
            session.setAttribute(name, value);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doDelete(HttpServletRequest requset, HttpServletResponse response) {
        HttpSession session = requset.getSession();
        String name = requset.getPathInfo().substring(1);
        if (session != null && session.getAttribute(name) != null) {
            session.removeAttribute(name);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}