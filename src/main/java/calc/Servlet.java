package calc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Servlet", urlPatterns = "/calc")
public class Servlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // get equation with vars from request
        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        String equation = String.join("", parameterMap.remove("equation")).replace(" ", "");

        // replace vars with corresponding values
        for(Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = String.join("", entry.getValue());
            if(value.length() == 1 && Character.isAlphabetic(value.charAt(0))) {
                value = String.join("", parameterMap.get(value));
            }
            equation = equation.replace(entry.getKey(), value);
        }

        // solve given equation
        Calc calc = new Calc();
        int answer = calc.getResult(equation);

        // print it out
        response.getWriter().println(answer);
    }
}
