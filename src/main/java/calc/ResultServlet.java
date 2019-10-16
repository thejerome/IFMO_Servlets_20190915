package calc;

import util.Parser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(
        urlPatterns = {"/calc/result"}
)

public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        String equation = (String) session.getAttribute("equation");
        String[] vars = (String[]) session.getValueNames();
        if (equation != null) {

            Map<String, Object> map = new HashMap<>();
            for (String s : vars) {
                if (!"equation".equals(s)) {
                    String value = (String) session.getAttribute(s);
                    if (value.matches("[a-zA-Z]+")) {
                        map.put(s, value);
                    } else {
                        int v = Integer.parseInt(value);
                        map.put(s, v);
                    }
                }
            }
            Set<String> variables = map.keySet();
            int i = 0;
            Map<String, String> matches = new HashMap<>();
            for (String s : variables) {
                matches.put(s, "x" + Integer.toString(i));
                i++;
            }
            for (String s : variables) {
                equation = equation.replaceAll(s, matches.get(s));
            }
            Integer test = 1;
            Map<String, Integer> mapa = new HashMap<>();
            for (String s : variables) {
                mapa.put(s, (int) (map.get(s).getClass() != test.getClass() ? map.get(map.get(s)) : map.get(s)));
            }
            double[] v = new double[variables.size()];
            i = 0;
            for (String s : variables) {
                v[i] = mapa.get(s);
                i++;
            }
            Parser estimator = new Parser();
            try {
                estimator.compile(equation);
                out.print((int) estimator.calculate(v));
                resp.setStatus(200);
            } catch (Exception e) {
                out.println("lack of data");
                resp.setStatus(409);
            }
        } else {
            resp.setStatus(409);
            resp.getWriter().println("lack of data");
        }
    }
}
