package com.mishep.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "PutVarServlet",
        urlPatterns = {"/calc/*"}
)
public class MethodsVarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String body = req.getReader().readLine();
            boolean isGoodFormatted;
            if (body.charAt(0) >= 'a' && body.charAt(0) <= 'z' && body.length() == 1)
                isGoodFormatted = true;
            else isGoodFormatted = Integer.parseInt(body) >= -10000 && Integer.parseInt(body) <= 10000;
            if (isGoodFormatted) {
                String val = String.valueOf((req.getRequestURI()).charAt((req.getRequestURI()).length() - 1));
                if ((req.getSession(false)).getAttribute(val) == null)
                    resp.setStatus(201);
                else
                    resp.setStatus(200);
                (req.getSession(false)).setAttribute(val, body);
            } else {
                resp.setStatus(403);
                resp.getWriter().write(" ");
            }
            resp.getWriter().flush();
            resp.getWriter().close();
            req.getReader().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setStatus(204);
        (req.getSession()).removeAttribute(String.valueOf((req.getRequestURI()).charAt((req.getRequestURI()).length()-1)));
    }
}
