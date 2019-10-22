package com.th1rt33nth.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "EqServlet",
        urlPatterns = {"/calc/equation"}
)
public class EqServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String eq = req.getReader().readLine();
        if (session.getAttribute("equation") == null)
            resp.setStatus(201); //https://music.yandex.ru/album/6684321/track/48920053
        else
            resp.setStatus(200);
        session.setAttribute("equation", eq);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession(false).removeAttribute("equation");
        resp.setStatus(204);
    }
}