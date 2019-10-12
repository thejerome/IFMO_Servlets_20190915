package com.weblab.rbetik12.servlets;

import com.weblab.rbetik12.utils.CalcUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc/result"}
)
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("equation") == null) {
            resp.setStatus(409);
            resp.getWriter().write("Equation wasn't set");
        } else if (httpSession.getAttribute("vars") == null && Pattern.matches("[a-z]", (String) httpSession.getAttribute("equation"))) {
            resp.setStatus(409);
            resp.getWriter().write("Not enough variables was set");
        } else {
            String equation = (String) httpSession.getAttribute("equation");
            Map<String, Object> varsMap = (HashMap<String, Object>) httpSession.getAttribute("vars");
            try {
                int result = CalcUtil.parse(equation, varsMap);
                resp.getWriter().write(Integer.toString(result));
            } catch (Exception e) {
                resp.setStatus(409);
                resp.getWriter().write("Not enough variables to calculate");
            }
        }
    }
}
