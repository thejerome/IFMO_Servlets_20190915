package com.webprog.task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "PutEquationServlet",
        urlPatterns = {"/calc/equation"}
)
public class PutAndDeleteEquationServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        BufferedReader reader = req.getReader();
        PrintWriter writer = resp.getWriter();
        String body = reader.readLine();
        if (session == null){
            session = req.getSession();
        }
        if (isGoodFormated(body)) {
            if (session.getAttribute("equation") == null)
                resp.setStatus(201);
            else
                resp.setStatus(200);
            session.setAttribute("equation", body);
        } else {
            resp.setStatus(400);
            writer.write("BAD FORMAAAT");
        }
        writer.flush();
        writer.close();
        reader.close();
    }

    private boolean isGoodFormated(String body) {
        /// trololololololololo
        return !body.equals("bad format");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(204);
        HttpSession session = req.getSession(false);
        if (session != null){
            session.removeAttribute("equation");
        }
    }


}
