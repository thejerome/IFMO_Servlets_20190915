package kemoler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@WebServlet(urlPatterns = {"/calc"})
public class MainServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Enumeration<String> reqParameterNames = req.getParameterNames();
        Map<String, String> map = new HashMap<>();

        while(reqParameterNames.hasMoreElements()) {
            String key = reqParameterNames.nextElement();
            String val = req.getParameterValues(key)[0];
            map.put(key, val);
        }
        String equation = map.get("equation");

        PrintWriter writer = resp.getWriter();
        writer.write(Calculator.eval(Objects.requireNonNull(Calculator.makePostfix(equation)), map));
    }

}
