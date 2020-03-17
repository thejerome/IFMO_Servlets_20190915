package com.burda;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)

public class CalcServlet extends HttpServlet {

    private String priorityList [][] = {{"*","/"}, {"+"}};

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        PrintWriter out = resp.getWriter();

        final Map<String, String[]> parameterMap= req.getParameterMap();

        String equation = req.getParameter("equation").replace(" ", "");

        while (!Pattern.matches("^[0-9*+-/()]+$", equation)){
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()){
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }

        out.println(evaluate(equation));

        out.flush();
        out.close();
    }

    private Integer evaluate(String data){

        String parsedData = data.replace("-","+-");

        while (parsedData.contains("(")){
            int start = parsedData.lastIndexOf("(");
            int finish = parsedData.indexOf(")", start);

            String temp = parsedData.substring(start, finish+1);
            parsedData = parsedData.replace(temp, countMicro(temp.replace("(","").replace(")","")).toString());
        }

        return (countMicro(parsedData));
    }

    private boolean containsOperation(String data){
        return data.contains("+") || data.contains("/") || data.contains("*");
    }

    private Integer countMicro(String data){

        String temp = data;

        int currentPriority = 0;

        while (containsOperation(temp)){
            String t1 = "";
            String t2 = "";
            String t3 = "";

            List<String> list = Arrays.asList(priorityList[currentPriority]);

            for (int i=0;i<temp.length();i++){
                String curChar = Character.toString(temp.charAt(i));

                if (list.contains(curChar)){
                    if (t2.length() > 0){
                        temp = temp.replace(t1 + t2 + t3, countSome(t2,t1,t3).toString());
                    }else{
                        t2 = curChar;
                    }
                }else if(containsOperation(curChar)){
                    if (t2.length() > 0){
                        temp = temp.replace(t1 + t2 + t3, countSome(t2,t1,t3).toString());
                    }else{
                        t1 = "";
                        t3 = "";
                    }
                }else {
                    if (t2.length() > 0){
                        t3 += curChar;
                    }else{
                        t1 += curChar;
                    }
                }
            }

            if (t1.length() > 0 && t2.length() >0 && t3.length()>0){
                temp = temp.replace(t1 + t2 + t3, countSome(t2,t1,t3).toString());
            }

            currentPriority++;
            if (currentPriority >= priorityList.length){
                if (containsOperation(temp)){
                    currentPriority = 0;
                }else{
                    break;
                }
            }
        }

        return Integer.parseInt(temp);
    }

    private Integer countSome(String ts,String vs1, String vs2){
        int val=0;

        int v1 = Integer.valueOf(vs1);
        int v2 = Integer.valueOf(vs2);

        if (ts.contains("*")) val =  v1 * v2;
        if (ts.contains("/"))val =  v1 / v2;
        if (ts.contains("+"))val = v1 + v2;

        return val;
    }
}