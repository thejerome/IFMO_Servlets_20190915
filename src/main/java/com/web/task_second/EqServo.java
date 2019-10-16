package com.web.task_second;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(
        name = "EqServo",
        urlPatterns = {"/calc/equation"}
)
public class EqServo extends HttpServlet {
    boolean properequation(String req) {
        Matcher matcher = Pattern.compile("[A-Z]+").matcher(req);
        Matcher matcher1 = Pattern.compile("[-+*/]").matcher(req);
        if (matcher.find() || !matcher1.find()) {
            return false;
        }
        return true;
    }

    @Override
    protected void doDelete(HttpServletRequest sreq, HttpServletResponse ans) {
        ans.setStatus(204);
        HttpSession s = sreq.getSession(false);
        if (s != null) {
            s.removeAttribute("equation");
        }
    }

    @Override
    protected void doPut(HttpServletRequest sreq, HttpServletResponse sresp) throws IOException {
        HttpSession s = sreq.getSession(false);
        BufferedReader r = sreq.getReader();
        PrintWriter w = sresp.getWriter();
        String b = r.readLine();
        if (s == null) {
            s = sreq.getSession();
        }
        if (properequation(b)) {
            if (s.getAttribute("equation") == null)
                sresp.setStatus(201);
            else
                sresp.setStatus(200);
            s.setAttribute("equation", b);
        } else {
            sresp.setStatus(400);
            w.write("Bad formatted equation");
        }
        w.flush();
        w.close();
        r.close();
    }


}
