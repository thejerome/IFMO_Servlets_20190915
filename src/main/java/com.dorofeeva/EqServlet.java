package com.dorofeeva;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(
        name = "EqServlet",
        urlPatterns = {"/calc/equation"}
)
public class EqServlet extends HttpServlet{
    @Override
    protected void  doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        HttpSession session = req.getSession();

        String equation = req.getReader().readLine();
        equation = equation.replace(" ", "");
        if (session.getAttribute("equation") == null)
            resp.setStatus(201);
        else
            resp.setStatus(200);
        session.setAttribute("equation", equation);
    }
    @Override
    protected void  doDelete(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        session.removeAttribute("equation");
        resp.setStatus(204);
    }

}
