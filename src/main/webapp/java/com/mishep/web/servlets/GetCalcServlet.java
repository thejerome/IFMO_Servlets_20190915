package com.mishep.web.servlets;

import com.mishep.web.utils.GetCalculate;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "GetCalcServlet",
        urlPatterns = {"/calc/result"}
)
public class GetCalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String result = "";
            String tokenStr = GetCalculate.getReversePolishNotation(((String) req.getSession().getAttribute("equation")).replaceAll("\\s", ""));
            try {
                result = GetCalculate.getResult(req, tokenStr);
            } catch (IllegalArgumentException e) {
                resp.setStatus(409);
            } finally {
                if (resp.getStatus() != 409) {
                    resp.setStatus(200);
                    (resp.getWriter()).write(result);
                }
            }
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}