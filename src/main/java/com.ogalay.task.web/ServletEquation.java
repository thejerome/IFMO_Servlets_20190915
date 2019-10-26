package com.ogalay.task.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet (urlPatterns = {"/calc/equation"})

public class ServletEquation extends HttpServlet{

    public static boolean equationCheck(String expr){
        return ('a' <= expr.charAt(0) && expr.charAt(0) <= 'z' && 'a' <= expr.charAt(1) && expr.charAt(1) <= 'z');
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        String eq = request.getReader().readLine();
        if (equationCheck(eq)){
            response.setStatus(400);
            response.getWriter().println("wrong data");
        }
        else if (session.getAttribute("equation") == null){
            response.setStatus(201);
            session.setAttribute("equation", eq);
        }
        else {
            response.setStatus(200);
            session.setAttribute("equation", eq);}
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        HttpSession session = request.getSession();
        session.setAttribute("equation", null);
        response.setStatus(204);
        //forCommit
    }
}