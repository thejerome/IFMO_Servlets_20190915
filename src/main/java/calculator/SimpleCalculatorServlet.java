package calculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/calc"})
public class SimpleCalculatorServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> hashMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        PrintWriter out = response.getWriter();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String val = request.getParameterValues(key)[0];
            hashMap.put(key, val);
        }
        String initialEquation = hashMap.get("equation");
//        System.out.println(initialEquation);
        try {
            out.write(SimpleCalculator.getResult(initialEquation, hashMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
