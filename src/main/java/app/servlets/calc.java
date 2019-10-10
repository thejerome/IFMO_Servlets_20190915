package app.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(
        name = "calc",
        urlPatterns = {"/calc"}
)
public class calc extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        String equation = request.getParameter("equation");
        Map<String, String[]> http = request.getParameterMap();
        Set<String> params = http.keySet();

        String value = "";
        Map<String, Object> map = new HashMap<>();
        for (String s : params) {
            if (!s.equals("equation")) {
                value = request.getParameter(s);
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
            out.println((int) Math.floor(estimator.calculate(v)));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
