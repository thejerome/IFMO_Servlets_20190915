package com.misha.task2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Task2Servlet", urlPatterns = "/calc/*")
public class Task2Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        final Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributesMap = new HashMap<>();
        while (attributeNames.hasMoreElements()) {
            String s = attributeNames.nextElement();
            attributesMap.put(s, session.getAttribute(s).toString());
        }
        String equation = attributesMap.remove("equation");
        for (Map.Entry<String, String> entry :
                attributesMap.entrySet()) {
            String value = entry.getValue();
            if (value.length() == 1 && Character.isAlphabetic(value.charAt(0))) {
                value = attributesMap.get(value);
            }
            equation = equation.replace(entry.getKey(), value);
        }
        response.getWriter().print(MyCalculator.instance().calculateEquation(equation));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo().substring(1);
        String body = req.getReader().readLine().replace(" ","");
        HttpSession reqSession = req.getSession();
        if (reqSession.getAttribute(pathInfo) == null) resp.setStatus(201);
        else resp.setStatus(200);
        reqSession.setAttribute(pathInfo, body);
        resp.getWriter().print(body);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute(req.getPathInfo().substring(1));
        resp.setStatus(204);
    }

}
