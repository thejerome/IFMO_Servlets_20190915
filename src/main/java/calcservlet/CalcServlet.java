package calcservlet;

import calculator.Calculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(name = "calcservlet.CalcServlet", urlPatterns = "/calc")

public class CalcServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String equation = request.getParameter("equation");
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            String var = params.get(key)[0];
            if (!"equation".equals(key)) {
                if (1 == var.length() && (var.charAt(0) >= 'a' && var.charAt(0) <= 'z')) {
                    var = params.get(var)[0];
                }
                equation = equation.replace(key, var);
            }
        }
        pw.println(Calculator.eval(equation));
    }
}
