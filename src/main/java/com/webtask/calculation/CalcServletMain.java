package com.webtask.calculation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(name = "CalcServletMain", urlPatterns = {"/calc"})
public class CalcServletMain extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        PrintWriter out = resp.getWriter();

        String equation = req.getParameter("equation");
        Map<String, String[]> valueMap = req.getParameterMap();
        StringBuilder equation_ = new StringBuilder();

        for(int i=0; i<equation.length(); ++i){
            if(Character.isLowerCase(equation.charAt(i))){
                String value = valueMap.get(Character.toString(equation.charAt(i)))[0];
                while(!isNumeric(value)){
                    value = valueMap.get(value)[0];
                }
                equation_.append(value);
            }
            else equation_.append(equation.charAt(i));
        }

        equation = equation_.toString();
        out.println(cal(equation));
    }

    public boolean isNumeric(String str){
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String cal(String src) {
        StringBuilder builder = new StringBuilder();
        if (src.contains("(")) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\(([^()]+)\\)");
            java.util.regex.Matcher matcher = pattern.matcher(src);
            int lastEnd = 0;
            while (matcher.find()) {

                builder.append(src.substring(lastEnd, matcher.start()));
                builder.append(cal(matcher.group(1)));
                lastEnd = matcher.end();
            }
            builder.append(src.substring(lastEnd));
        } else {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(-?[\\d.]+)\\s*([*/])\\s*(-?[\\d.]+)");
            builder.append(src);
            java.util.regex.Matcher matcher = pattern.matcher(builder.toString());
            while (matcher.find()){
                float f1 = Float.parseFloat(matcher.group(1));
                float f2 = Float.parseFloat(matcher.group(3));
                long result = 0;
                switch (matcher.group(2)){
                    case "*":
                        result = (long)(f1 * f2);
                        break;
                    case "/":
                        result = (long)(f1 / f2);
                        break;
                    default:
                        result = 0;
                        break;
                }
                builder.replace(matcher.start(), matcher.end(),
                        String.valueOf(result));
                matcher.reset(builder.toString());
            }
            pattern = java.util.regex.Pattern.compile("([\\d.]+)\\s*([+-])\\s*([\\d.]+)");
            matcher = pattern.matcher(builder.toString());
            while (matcher.find()){
                float f1 = Float.parseFloat(matcher.group(1));
                float f2 = Float.parseFloat(matcher.group(3));
                long result = 0;
                switch (matcher.group(2)){
                    case "+":
                        result = (long)(f1 + f2);
                        break;
                    case "-":
                        result = (long)(f1 - f2);
                        break;
                    default:
                        result = 0;
                        break;
                }
                builder.replace(matcher.start(), matcher.end(),
                        String.valueOf(result));
                matcher.reset(builder.toString());
            }
            return builder.toString();
        }
        return cal(builder.toString());
    }
}
