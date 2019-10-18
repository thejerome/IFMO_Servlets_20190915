package com.sam.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "ServletPADEquation",
        urlPatterns = {"/calc/equation"}
)

public class PutAndDeleteEquation extends HttpServlet  {
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession thisSession = req.getSession();
        String equation = req.getReader().readLine();

        if (thisSession.getAttribute("equation") != null) //сессия существует
            resp.setStatus(200);
        else resp.setStatus(201);

        thisSession.setAttribute("equation", equation);

    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession thisSession = req.getSession();
        if (thisSession.getAttribute("equation") != null){
            thisSession.setAttribute("equation", null);
            resp.setStatus(204);
        }
    }
}
