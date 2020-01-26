package servlets;

import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(
        name = "ValueServlet",
        urlPatterns = "/calc/*"
)
public class ValueServlet extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

            String value = request.getAttribute("value").toString();
            String url = request.getRequestURI();
            int index = url.length() - 1;
            String key = url.substring(index);

            HttpSession session = request.getSession(false);
            Enumeration<String> e = session.getAttributeNames();
            boolean valueExists = false;

            if(e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String name = (String) e.nextElement();
                    if(key.equals(name)) {
                        valueExists = true;
                        session.setAttribute(key, value);
                        response.setStatus(200);
                    }
                }
            }
            if(!valueExists) {
                session.setAttribute(key,value);
                response.setStatus(201);
            }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {

        String url = request.getRequestURI();
        int index = url.length() - 1;
        String key = url.substring(index);

        HttpSession oldSession = request.getSession(false);
        oldSession.removeAttribute(key);
        response.setStatus(204);
    }
}
