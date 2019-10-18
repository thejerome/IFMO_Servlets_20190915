package com.webtask.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PutDeleteServlet", urlPatterns = {"/calc/*"})
public class ParamentServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String name = String.valueOf(req.getRequestURI().charAt(6));
        String value_ = req.getReader().readLine();
        if(session.getAttribute("paraments") == null) session.setAttribute("paraments", new HashMap<String, String>());
        Map<String, String> valueMap = (HashMap<String, String>)session.getAttribute("paraments");
        if(bigValue(value_)){
            resp.setStatus(403);
        } else {
            if (valueMap.containsKey(name)) {
                resp.setStatus(200);
            }
            else resp.setStatus(201);
            valueMap.put(name,value_);
            session.setAttribute("paraments", valueMap);
        }
    }

    private boolean bigValue(String value){
        if(value.charAt(0) >='a' && value.charAt(0)<='z') return false;
        return Integer.parseInt(value)>10000 || Integer.parseInt(value)<-10000;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        HttpSession session = req.getSession();
        Map<String, String> valueMap = (HashMap<String, String>)session.getAttribute("paraments");
        String name = String.valueOf(req.getRequestURI().charAt(6));
        valueMap.remove(name);
        resp.setStatus(204);
    }
}
