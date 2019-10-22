package com.th1rt33nth.ifmo.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = "/calc/result"
)
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        try {
            String eq = session.getAttribute("equation").toString();
            if (eq == null)
                throw new ServletException("We fucked up, equation not found");
            eq = eq.replace(" ", "");
            Map<String, String> sessionVars = new HashMap();
            Map<String, String> eqVars = new HashMap();
            toMap (session, eq, sessionVars, eqVars);
            replaceVal (eqVars, sessionVars);
            for (int i = 0; i < eq.length(); i++) {
                if (eq.charAt(i) >= 'a' && eq.charAt(i) <= 'z')
                    eq = eq.replace(Character.toString(eq.charAt(i)),
                            eqVars.get(Character.toString(eq.charAt(i))));
            }
            out.print(Auxilliary.calc(eq));
            resp.setStatus(200);
        }
        catch (ServletException e) {
            resp.setStatus(409);
            out.print(e.getMessage());
        }
        finally {
            out.flush();
            out.close();
        }
    }
    private void toMap(HttpSession session, String eq, Map<String, String> sessionVars, Map<String, String> eqVars) {
        Enumeration<String> attributes = session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String var = attributes.nextElement();
            sessionVars.put(var, session.getAttribute(var).toString());
        }
        sessionVars.remove("equation");
        for (int i = 0; i < eq.length(); i++) {
            if (eq.charAt(i) >= 'a' && eq.charAt(i) <= 'z')
                eqVars.put(Character.toString(eq.charAt(i)), "");
        }
    }
    private void replaceVal (Map<String, String> eqVars, Map<String, String> sessionVars)
            throws ServletException {
        for (Map.Entry<String, String> var : eqVars.entrySet()) {
            String key = var.getKey();
            String val = sessionVars.get(key);
            if (val == null)
                throw new ServletException("We fucked up, var not found");
            while (!Pattern.matches("^[-0-9]+$", val)) {
                key = val;
                val = sessionVars.get(key);
                if (val == null)
                    throw new ServletException("We fucked up, var not found");
            }
            var.setValue(val);
        }
    }
}