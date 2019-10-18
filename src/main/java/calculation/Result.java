package calculation;


import instruments.Calculator;

import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (urlPatterns = {"/calc/result"})

public class Result extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String eq = (String) session.getAttribute("equation");

        if (eq != null) {
            //out.println(eq);

            String ans = "";
            Map<String, String> parameterMap = new HashMap<>();
            //Map<String, Integer> fail = new HashMap<>();
            for (String s : session.getValueNames()) {
                if (s != "equation") {
                    String v = (String) session.getAttribute(s);
                    if (v.charAt(0) == '-') {
                        ans = "10000" + v.substring(1);
                        parameterMap.put(s, ans);
                    } else {
                        parameterMap.put(s, v);
                    }


                }


            }


            StringBuilder example = new StringBuilder();


            //out.println(parameterMap.entrySet());

            for (int i = 0; i < eq.length(); ++i) {
                char cur = eq.charAt(i);
                //out.println(cur);
                if (cur >= 'a' && cur <= 'z') {
                    String newVal = parameterMap.get(Character.toString(cur));
                    if (newVal == null) break;
                    while (!Calculator.isDigit(newVal)) {
                        newVal = parameterMap.get(newVal);

                    }
                    example.append(newVal);
                } else {
                    example.append(eq.charAt(i));
                }
                //out.println(example);
            }
            //out.println(example);
            eq = example.toString();


            try {
                out.print(Calculator.counting(Calculator.getExpression(eq)));
                response.setStatus(200);
            }
            catch (Exception e){
                response.setStatus(409);
                out.println("lack of data");
            }
        }
        else {
            response.setStatus(409);
            response.getWriter().println("lack of data");
        }
        //out.println(eq);
        //String exit = Calculator.getExpression(eq);
        //out.println(exit);
        //out.print(Calculator.counting(exit));




    }
}