package com.web.task_second;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "VarServo",
        urlPatterns = {"/calc/*"}
)
public class VarServo extends HttpServlet {
    private boolean propervar(String str) throws NumberFormatException {
        if (str.matches("[a-zA-Z]") && str.length() == 1)
            return true;
        int res = Integer.parseInt(str);
        return res >= -10000 && res <= 10000;
    }

    @Override
    protected void doDelete(HttpServletRequest sreq, HttpServletResponse ans) {
        ans.setStatus(204);
        String url = sreq.getRequestURI();
        HttpSession s = sreq.getSession(false);
        if (s != null) {
            s.removeAttribute(String.valueOf(url.charAt(url.length() - 1)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest sreq, HttpServletResponse sresp) throws IOException {
        HttpSession s = sreq.getSession(false);
        String url = sreq.getRequestURI();
        BufferedReader r = sreq.getReader();
        PrintWriter w = sresp.getWriter();
        String b = r.readLine();
        if (s == null) {
            s = sreq.getSession(true);
        }
        if (!propervar(b)) {
            sresp.setStatus(403);
            w.write("Value overflow");
            w.flush();
            return;
        }

        String x = String.valueOf(url.charAt(url.length() - 1));
        if (s.getAttribute(x) == null)
            sresp.setStatus(201);
        else
            sresp.setStatus(200);
        s.setAttribute(x, b);

        w.flush();
    }


}
