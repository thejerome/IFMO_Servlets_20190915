package com.dorofeeva;

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        HttpSession session = req.getSession();
        String var = req.getRequestURI().substring(6);
        String val = req.getReader().readLine();
        System.out.print(var);
        System.out.print(' ');
        System.out.println(val);

        String old;
        StringBuilder VList = new StringBuilder();

        if (VarCor(val)){
            if (session.getAttribute(var) == null){
                resp.setStatus(201);
            }
            else {
                resp.setStatus(200);
            }

            session.setAttribute(var, val);

            if (session.getAttribute("varList") != null) {
                old = session.getAttribute("varList").toString();
                VList.append(old);
            }

            VList.append(var);
            session.setAttribute("varList", VList.toString());

        }
        else
            resp.setStatus(403);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        session.removeAttribute(String.valueOf(req.getRequestURI().charAt(6)));
        resp.setStatus(204);
    }

    private boolean VarCor (String var){
        boolean f;
        if (var.charAt(0)>='a' && var.charAt(0)<='z') {
            return true;
        }
        f = Integer.parseInt(var) >= -10000 && Integer.parseInt(var) <= 10000;
        return f;
    }
}