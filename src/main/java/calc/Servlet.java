package calc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CalcServlet", urlPatterns = "/calc/*")
public class Servlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String pathInfo = req.getPathInfo().substring(1);
        final String line = req.getReader().readLine().replace(" ", "");
        HttpSession session = req.getSession();
        if (session.getAttribute(pathInfo) == null) {
            resp.setStatus(201);
        }
        session.setAttribute(pathInfo, line);
        req.getReader().reset();
        resp.getWriter().printf("Set %s to %s%n", pathInfo, line);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Calc calc = new Calc();
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        // get equation with vars from session
        final Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, String> attributesMap = new HashMap<>();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributesMap.put(attributeName, session.getAttribute(attributeName).toString());
        }
        String equation = attributesMap.remove("equation");

        // replace vars with corresponding values
        for (Map.Entry<String, String> entry :
                attributesMap.entrySet()) {
            String value = entry.getValue();
            if (value.length() == 1 && Character.isAlphabetic(value.charAt(0))) {
                value = attributesMap.get(value);
            }
            equation = equation.replace(entry.getKey(), value);
        }

        // solve given equation
        int answer = calc.getResult(equation);
        out.print(answer);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String attributeName = req.getPathInfo().substring(1);
        req.getSession().removeAttribute(attributeName);
        resp.setStatus(204);
    }
}

