package kemoler;

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
import java.util.Objects;


@WebServlet(urlPatterns = {"/calc/result"})
public class MainServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        Enumeration<String> sessionParameterNames = session.getAttributeNames();
        Map<String, String> map = new HashMap<>();

        if (!sessionParameterNames.hasMoreElements()) resp.sendError(403, "error");
        while(sessionParameterNames.hasMoreElements()) {
            String key = sessionParameterNames.nextElement();
            map.put(key, (String) session.getAttribute(key));
        }
        String equation = map.get("equation");
        System.out.println(equation);
        System.out.println(map.toString());
        PrintWriter writer = resp.getWriter();

        if (equation != null) {
            try {
                writer.write(Calculator.eval(Objects.requireNonNull(Calculator.makePostfix(equation)), map));
            } catch (Exception e) {
                resp.sendError(409);
            }
        } else {
            resp.sendError(409);
        }

    }

}
