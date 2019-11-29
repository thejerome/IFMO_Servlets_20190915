package com.mishep.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "PutEquationServlet",
        urlPatterns = {"/calc/equation"}
)
public class MethodsEquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String body = (req.getReader()).readLine();
            boolean notBadFormat = true;
            int i = 0;
            do {
                if (body.charAt(i) >= 'a' && body.charAt(i) <= 'z' && i < body.length() - 1 && (body.charAt(i + 1) >= 'a' && body.charAt(i + 1) <= 'z')) {
                    notBadFormat = false;
                    break;
                }
                i++;
            } while (i < body.length());
            if (notBadFormat) {
                if ((req.getSession()).getAttribute("equation") == null)
                    resp.setStatus(201);
                else
                    resp.setStatus(200);
                (req.getSession()).setAttribute("equation", body);
            } else {
                resp.setStatus(400);
                (resp.getWriter()).write(" ");
            }
            (resp.getWriter()).flush();
            (resp.getWriter()).close();
            (req.getReader()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(204);
        req.getSession().removeAttribute("equation");
    }
}
