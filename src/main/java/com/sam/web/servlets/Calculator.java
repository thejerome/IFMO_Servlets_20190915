package com.sam.web.servlets;

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

@WebServlet(
        name = "Calculator",
        urlPatterns = {"/calc/result"}
)
public class Calculator extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession thisSession = req.getSession();
        String equation = (String) thisSession.getAttribute("equation");
        if (equation == null) {
            resp.setStatus(409);
            resp.getWriter().write("Выражения нет");
        } //else if (thisSession.getAttribute("name") == null) {
        // resp.setStatus(409);
        //resp.getWriter().write("Недостаточно переменных");
        //}
        else {
            Map<String, String> prmtrs = new HashMap<>();
            Enumeration<String> list = thisSession.getAttributeNames();

            while (list.hasMoreElements()) {
                String buff = list.nextElement();
                if (buff.compareTo(equation) != 0) //=0 в случае равенства baff and equation
                    prmtrs.put(buff, (String) thisSession.getAttribute(buff));
            }
            try {
                equation = ParserUtils.mapping(thisSession);
                resp.getWriter().print(ParserUtils.answerMthd(ParserUtils.parse(equation)));
                resp.setStatus(200);
            } catch (IllegalArgumentException e) {
                resp.setStatus(409);
            }
        }
    }

}