package calculator;

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


@WebServlet(urlPatterns = {"/calc/result"})
public class ComplexResServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession httpSession = request.getSession(false);
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        Map<String, String> map = new HashMap<>();
        PrintWriter out = response.getWriter();

        if (!attributeNames.hasMoreElements())
            response.sendError(403, "");
        while(attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            String  value = (httpSession.getAttribute(key)).toString();
            map.put(key, value);
        }
        String equation = map.get("equation");
        if (equation != null) {
            try {
                out.write(SimpleCalculator.getResult(equation, map));
            } catch (IllegalStateException e) {
                response.sendError(409, "");
            }
        } else {
            response.sendError(409, "");
        }

    }

}
