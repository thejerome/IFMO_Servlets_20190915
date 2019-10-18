package com.web.task;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "VarsServlet",
        urlPatterns = {"/calc/*"}
)

public class VarsServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        String val = req.getReader().readLine();
        String URI = req.getRequestURI();
        String var = String.valueOf(URI.charAt(URI.length() - 1));
        if (correctValue(val)) {
            if (session.getAttribute(var) == null) {
                resp.setStatus(201);
                session.setAttribute(var, val);
            }
            else {
                resp.setStatus(200);
                session.setAttribute(var, val);
            }
        }
        else {
            resp.setStatus(403);
            writer.println("value isn't correct or out of range");
        }

        writer.flush();
        writer.close();
    }

    @Override
    protected void doDelete (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String URI = req.getRequestURI();
        String var = String.valueOf(URI.charAt(URI.length() - 1));
        session.removeAttribute(var);
        resp.setStatus(204);
    }



    private boolean correctValue(String val) {
        return (val.charAt(0) >= 'a' && val.charAt(0) <= 'z') || (Integer.parseInt(val) >= -10000 && Integer.parseInt(val) <= 10000);
    }
}
