package com.marycia.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (
        name = "ResultServlet",
        urlPatterns = {"/calc/result"}
)
public class ResultServlet extends HttpServlet {

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        try {
            out.print(Calculate.calculateResult(req,resp));
            out.flush();
        } catch (IllegalArgumentException e) {
            resp.setStatus(409);
        }

    }
}
