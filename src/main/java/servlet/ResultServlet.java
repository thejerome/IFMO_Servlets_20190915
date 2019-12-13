package servlet;

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

@WebServlet(name = "ResultServlet", urlPatterns = "/calc/result")
public class ResultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        HttpSession currSession = request.getSession();
        final Enumeration<String> attributeNames = currSession.getAttributeNames();
        Map<String, String> vars = new HashMap<>(); // form a hash map with vars for calc algorithm
        while(attributeNames.hasMoreElements()) {
            String variableName = attributeNames.nextElement();
            vars.put(variableName, currSession.getAttribute(variableName).toString());
        }
        String equation = Calc.replaceVars(vars);
        out.print(Calc.evaluate(equation));
    }
}
