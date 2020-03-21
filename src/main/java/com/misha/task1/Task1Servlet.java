package com.misha.task1;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Task1Servlet", urlPatterns = "/calc")
public class Task1Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        String equation = parameterMap.remove("equation")[0];
        equation = equation.replace(" ", "");
        for (Map.Entry<String, String[]> param: parameterMap.entrySet()) {
            String value;
            if(Character.isAlphabetic(param.getValue()[0].charAt(0))) {
                value = parameterMap.get(param.getValue()[0])[0];
            } else {
                value = param.getValue()[0];
            }
            equation = equation.replace(param.getKey(), value);
        }
        int equationResult = MyCalculator.instance().calculateEquation(equation);
        response.getWriter().println(equationResult);
    }
}
