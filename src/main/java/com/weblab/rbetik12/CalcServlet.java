package com.weblab.rbetik12;

import parser.Parser;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String equation = req.getParameterMap().get("equation")[0];
        Map<String, Object> varsMap = new HashMap<>();
        for (Map.Entry<String, String[]> param: req.getParameterMap().entrySet()) {
            if (!param.getKey().equals("equation")) {
                varsMap.put(param.getKey(), param.getValue()[0]);
            }
        }
        int equationResult = Parser.parse(equation, varsMap);
        ServletOutputStream out = resp.getOutputStream();
        out.write(Integer.toString(equationResult).getBytes());
        out.flush();
        out.close();
    }
}