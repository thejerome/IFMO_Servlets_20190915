package com.pilacis.web.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

@WebServlet(
        name = "GetResult",
        urlPatterns = ("/calc/result")
)
public class GetResult extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws  IOException {

        try {
            // запускаем сессию
            HttpSession session = req.getSession();
            // отделяем name от переменных

            final String name = (String) session.getAttribute("equation");
            HashMap<String, String> variables = new HashMap();
            Enumeration<String> list = session.getAttributeNames();







            while (list.hasMoreElements()) {
                String buffer = list.nextElement();

                if (buffer.compareTo("equation") != 0) {
                    variables.put(buffer, (String) session.getAttribute(buffer));
                }
            }
            ///////////////////////////////////////////////////////////////

            PrintWriter out = resp.getWriter();

            //парс и вывод
            Parser parser = new Parser(name, variables);
            parser.parse();
            out.print(parser.evaluate());
            resp.setStatus(200);
            /////////////////////////////////////////////////////////////////

            out.flush();
            out.close();
        }catch(NumberFormatException ex){
            resp.setStatus(409);
        }
    }
}

