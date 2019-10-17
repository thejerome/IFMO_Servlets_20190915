package com.piskov.web.servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(
        name = "PutEquationServlet",
        urlPatterns = {"/calc/equation"}
)
public class PutAndDeleteEquation extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();
        final BufferedReader reader = req.getReader();
        final String equation = reader.readLine();
        System.out.println(equation);
            if (session.getAttribute("equation") == null) {
                System.out.println(201);
                resp.setStatus(201);
            } else {
                System.out.println(200);
                resp.setStatus(200);
            }
            session.setAttribute("equation", equation);
        }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(204);
        HttpSession session = req.getSession();
        session.removeAttribute("equation");
    }


}