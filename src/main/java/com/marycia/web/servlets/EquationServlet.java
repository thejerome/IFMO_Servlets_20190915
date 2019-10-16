package com.marycia.web.servlets;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet (
        name = "EquationServlet",
        urlPatterns = {"/calc/equation"}
)

public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();

        String value = req.getReader().readLine();

        boolean consistOfDel = false;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '/' || value.charAt(i) == '*' || value.charAt(i) == '-' || value.charAt(i) == '+' || value.charAt(i) == '(' || value.charAt(i) == ')')
                consistOfDel = true;
        }
        if (!consistOfDel) {
            resp.setStatus(400);
            resp.getWriter().print("Bad format. Try again");
        } else {
            if (session.getAttribute("equation") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }

            session.setAttribute("equation", value);
        }
    }
    @Override
    protected void doDelete (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        session.removeAttribute("equation");
        resp.setStatus(204);
    }

}
