package com.sam.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "ServletPADVar",
        urlPatterns = {"/calc/*"}
)
public class PutAndDeleteVariable extends HttpServlet {
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession thisSession = req.getSession();

        String name = String.valueOf(req.getRequestURI().charAt(6));
        String value = req.getReader().readLine();
        if (thisSession.getAttribute(name) != null) //сессия существует
            resp.setStatus(200);
        else resp.setStatus(201);
        thisSession.setAttribute(name, value);

        //+ошибку 403 в фильтр?
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp){
        HttpSession thisSession = req.getSession();
        String name = String.valueOf(req.getRequestURI().charAt(6));
        thisSession.setAttribute(name, null);
        resp.setStatus(204);

    }
}
