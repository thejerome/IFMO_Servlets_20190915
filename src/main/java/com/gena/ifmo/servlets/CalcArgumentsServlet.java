package com.gena.ifmo.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(
        name = "CalcArgumentServlet",
        urlPatterns = ("/calc/*")
)

public class CalcArgumentsServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if ( "PUT".equals(req.getMethod()) ){
            doPut(req, resp);
        }else if ( "DELETE".equals(req.getMethod()) ){
            doDelete(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)throws IOException{
        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }

        String thisPath = req.getRequestURI();
        String argumentName = thisPath.substring(thisPath.lastIndexOf('/') + 1);
        BufferedReader reader = req.getReader();
        String argumentValue = reader.readLine();
        if (req.getAttribute("403") != null){
            resp.setStatus(403);
            resp.getWriter().print(req.getAttribute("403"));
        }else if (req.getAttribute("400") != null){
            resp.setStatus(400);
            resp.getWriter().print(req.getAttribute("400"));
        }else{

            if (session.getAttribute(argumentName) == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            session.setAttribute(argumentName, argumentValue);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        resp.setStatus(204);
        HttpSession session = req.getSession(false);
        if (session != null){
            String thisPath = req.getRequestURI();
            String argumentName = thisPath.substring(thisPath.lastIndexOf('/') + 1);
            session.removeAttribute(argumentName);
        }

    }
}
