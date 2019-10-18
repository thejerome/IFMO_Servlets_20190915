package com.webtask.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "EquationServlet", urlPatterns = {"/calc/equation"})
public class EquationServlet extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        HttpSession session = req.getSession();
        String s = req.getReader().readLine();
        if(badlyFormatted(s)){
            resp.sendError(400, "Badly formatted!");
        }
        else {
            if(session.getAttribute("equation") == null){
                resp.setStatus(201);
            }
            else resp.setStatus(200);
            session.setAttribute("equation", s);
        }
    }

    private boolean badlyFormatted(String equation){
        Pattern pattern = Pattern.compile("([a-z]{2})|([-+*/]){2}");
        Matcher matcher = pattern.matcher(equation);
        return matcher.find();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        HttpSession session = req.getSession();
        session.setAttribute("equation", null);
        resp.setStatus(204);
    }
}
