package com.webprog.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "PutVarServlet",
        urlPatterns = {"/calc/*"}
)
public class PutAndDeleteVarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String requestURI = req.getRequestURI();
        BufferedReader reader = req.getReader();
        PrintWriter writer = resp.getWriter();
        String body = reader.readLine();
        if (session == null){
            session = req.getSession();
        }
        if (isGoodFormatted(body)) {
            String val = String.valueOf(requestURI.charAt(requestURI.length() - 1));
            if (session.getAttribute(val) == null)
                resp.setStatus(201);
            else
                resp.setStatus(200);
            session.setAttribute(val, body);
        } else {
            resp.setStatus(403);
            writer.write("Exceeding values");
        }
        writer.flush();
        writer.close();
        reader.close();
    }

    private boolean isGoodFormatted(String body) {
        if (body.charAt(0) >= 'a' && body.charAt(0) <= 'z' && body.length() == 1)
            return true;
        return Integer.parseInt(body) >= -10000 && Integer.parseInt(body) <= 10000;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setStatus(204);
        String requestURI = req.getRequestURI();
        HttpSession session = req.getSession(false);
        if (session != null){
            session.removeAttribute(String.valueOf(requestURI.charAt(requestURI.length()-1)));
        }
    }
}
