package task1;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(
        urlPatterns = "/calc",
        name = "Servlet"
)
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String eq = req.getParameter("equation").replace(" ", "");

        eq = clear(eq, req.getParameterMap());
        System.out.println(eq);
        resp.getOutputStream().print(Calculator.getResult(eq));
        resp.getOutputStream().flush();
    }

    private String clear(String expr1, Map<String, String[]> par) {
        Map<String, Object> var = new HashMap<>();
        String expr = expr1;
        for (Map.Entry<String, String[]> param : par.entrySet()) {
            if (!"equation".equals(param.getKey())) {
                var.put(param.getKey(), param.getValue()[0]);
            }
        }
        for (Map.Entry<String, Object> kek : var.entrySet()) {
            Object value = kek.getValue();
            if (value instanceof Integer) {
                expr = expr.replaceAll(kek.getKey(), kek.getValue().toString());
            } else {
                expr = expr.replaceAll(kek.getKey(), (String) kek.getValue());
            }

        }

        if (contains(expr)) {
            return clear(expr, par);
        }
        return expr;
    }

    private boolean contains(String str) {
        for (char i : str.toCharArray()) {
            if (i >= 'a' && i <= 'z') {
                return true;
            }
        }
        return false;
    }


}
