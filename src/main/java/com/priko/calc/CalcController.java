package com.priko.calc;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "Calculator",
        urlPatterns = {"/calc"}
)
public class CalcController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        String equation = request.getParameter("equation");

        Map<String, String[]> params = request.getParameterMap();
        //params.remove("equation");
        Map<String, String> vars = new HashMap<>();
        for(Map.Entry<String, String[]> param : params.entrySet()){
            vars.put(param.getKey(), param.getValue()[0]);
        }

        ShuntingClass sh = new ShuntingClass();

        sh.setVars(vars);
        sh.convertEq(equation);

        outputStream.write(sh.getEq().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
