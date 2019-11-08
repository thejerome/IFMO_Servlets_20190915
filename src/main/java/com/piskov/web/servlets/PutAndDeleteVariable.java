package com.piskov.web.servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(
        name = "PutVarServlet",
        urlPatterns = {"/calc/*"}
)
public class PutAndDeleteVariable extends HttpServlet {
        @Override //
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
            final HttpSession session = req.getSession();
            final String requestURI = req.getRequestURI();
            final BufferedReader reader = req.getReader();
            final String value = reader.readLine();
            req.getReader().reset();
            final String name = String.valueOf(requestURI.charAt(6));
            int status = (session.getAttribute(name) == null) ? 201:200;
            resp.setStatus(status);
            session.setAttribute(name, value);

        }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(204);
        HttpSession session = req.getSession();
        session.removeAttribute(String.valueOf(req.getRequestURI().charAt(6)));
    }
}