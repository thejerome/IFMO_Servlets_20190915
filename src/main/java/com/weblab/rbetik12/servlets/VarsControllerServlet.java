package com.weblab.rbetik12.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet(
        name = "VarsControllerServlet",
        urlPatterns = {"/calc/*"}
)
public class VarsControllerServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        String varName = req.getRequestURI().substring(6);
        String varVal = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (httpSession.getAttribute("vars") == null) {
            httpSession.setAttribute("vars", new HashMap<String, Object>());
        }
        Map<String, Object> varsMap = (HashMap<String, Object>) httpSession.getAttribute("vars");
        if (varsMap.containsKey(varName)) {
            resp.setStatus(200);
        } else {
            resp.setStatus(201);
        }
        varsMap.put(
                varName,
                Pattern.matches("[0-9]", varVal) ? Integer.parseInt(varVal) : varVal
        );
        httpSession.setAttribute("vars", varsMap);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();
        Map<String, Object> varsMap = (HashMap<String, Object>) httpSession.getAttribute("vars");
        String varName = req.getRequestURI().substring(6);
        varsMap.remove(varName);
        resp.setStatus(204);
    }
}
