package com.pilacis.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(
        name = "CalcEquation",
        urlPatterns = {"/calc/equation"}
)
public class SetEquation extends HttpServlet {


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        final String oldEquation = (String) session.getAttribute("equation");
        final String newEquation = req.getReader().readLine();
        resp.setStatus((oldEquation == null) ? 201:200);
        session.setAttribute("equation", newEquation);

    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        req.removeAttribute("equation");
    }



}
