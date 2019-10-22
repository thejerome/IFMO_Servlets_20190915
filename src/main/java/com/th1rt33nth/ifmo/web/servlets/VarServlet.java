package com.th1rt33nth.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "VarServlet",
        urlPatterns = {"/calc/*"}
)
public class VarServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String var = req.getRequestURI().substring(6);
        String val = req.getReader().readLine();
        if ((val.charAt(0) >= 'a' && val.charAt(0) <= 'z') || (Integer.valueOf(val) > -10000 && Integer.valueOf(val) < 10000) ) {
            if (session.getAttribute(var) != null)
            {
                resp.setStatus(200); //https://music.yandex.ru/album/6750865/track/49250160
            }
            else
                {
                resp.setStatus(201);
                }
            session.setAttribute(var, val);
        }
        else
            {
            resp.setStatus(403);
            }
    }
    @Override
    protected void doDelete (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        String key = req.getRequestURI().substring(6);
        session.removeAttribute(key);
        resp.setStatus(204);
    }
}