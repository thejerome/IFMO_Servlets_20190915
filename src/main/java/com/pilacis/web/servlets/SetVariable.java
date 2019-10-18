package com.pilacis.web.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(
        name = "SetVariable",
        urlPatterns = ("/calc/*")
)
public class SetVariable extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();

        final String variableName = req.getRequestURI().substring(6);
        String valueStr = req.getReader().readLine();

        // отправляем отчёт
        resp.setStatus(((String)session.getAttribute(variableName)) == null ? 201:200);
        session.setAttribute(variableName, valueStr);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute(req.getRequestURI().substring(6));
    }
}
