package com.efimchick.ifmo.web.servlets;

import org.apache.commons.codec.binary.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(
        name = "PutAndDeleteVariableServlet",
        urlPatterns = {"/calc/*"}
)
public class PutAndDeleteVariableServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String s = req.getReader().readLine();
        String name = req.getRequestURI().substring(6);
        if (!badFormatted(s)) {
            resp.setStatus(403);
            resp.getWriter().println("bad format");
        } else {
            if (session.getAttribute(name) == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            session.setAttribute(name, s);
        }
    }

    private boolean badFormatted(String s) {
        if (s.length() == 1 && s.charAt(0) >= 'a' && s.charAt(0) <= 'z')
            return true;
        try {
            int a = Integer.parseInt(s);
            return a>=-10000 && a<=10000;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute(req.getRequestURI().substring(6), null);
        resp.setStatus(204);
    }
}
