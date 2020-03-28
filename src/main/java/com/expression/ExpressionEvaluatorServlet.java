package com.expression;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/calc")
public class ExpressionEvaluatorServlet extends HttpServlet {

    private static final String EXPRESSION_PARAM_NAME = "equation";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String expression = req.getParameter(EXPRESSION_PARAM_NAME);
        String preparedExpression = prepareExpression(expression, req.getParameterMap());
        int result = new ExpressionEvaluator().evaluate(preparedExpression);
        resp.getOutputStream().print(result);
    }

    private String prepareExpression(String expression, Map<String, String[]> params) {
        String preparedString = expression;
        List<String> keys =
                params.keySet().stream()
                        .filter(key -> !EXPRESSION_PARAM_NAME.equalsIgnoreCase(key))
                        .sorted(Comparator.comparingInt(String::length).reversed())
                        .collect(Collectors.toList());

        for (String key : keys) {
            preparedString = preparedString.replaceAll(key, getParamValue(key, params));
        }
        return preparedString;
    }

    private String getParamValue(String key, Map<String, String[]> paramsMap) {
        String res = paramsMap.get(key)[0];
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        while (!pattern.matcher(res).matches()) {
            res = paramsMap.get(res)[0];
        }
        return res;
    }

}