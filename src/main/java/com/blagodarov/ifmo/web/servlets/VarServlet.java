package com.blagodarov.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(
        name = "VarServlet",
        urlPatterns = {"/calc/*"}
)

public class VarServlet extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        HttpSession session = req.getSession();
        String var = req.getRequestURI().substring(6);
        String value = req.getReader().readLine();
        System.out.print(var);
        System.out.print(' ');
        System.out.println(value);
        
        if (correctVar(value)){
            if (session.getAttribute(var) == null)
                resp.setStatus(201); //bad value
            else
                resp.setStatus(200); //good value
            session.setAttribute(var, value);
            //Working with string of var names, like "abcz" to make future easier
            StringBuilder varList = new StringBuilder();
            
            if (session.getAttribute("varList") != null) {
                String oldList = session.getAttribute("varList").toString();
                varList.append(oldList);
            }
            varList.append(var);
            session.setAttribute("varList", varList.toString());
            
        }
        else
            resp.setStatus(403); //incorrect value
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        HttpSession session = req.getSession();
        session.removeAttribute(String.valueOf(req.getRequestURI().charAt(6)));
        resp.setStatus(204);
    }
    
    private boolean correctVar (String var){
        if (var.charAt(0)>='a' && var.charAt(0)<='z') {
            return true;
        }
        return Integer.parseInt(var) >= -10000 && Integer.parseInt(var) <= 10000;
    }
}
