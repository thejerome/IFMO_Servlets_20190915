package test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "ServletCalculator",
        urlPatterns = {"/calc/result"}
)
public class ServletCalculator extends HttpServlet {

    private String priorityList [][] = {{"*","/"}, {"+"}};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(false);

        if (session.getAttribute("equation") == null) {
            resp.setStatus(409);
            out.println("no equation");
        }else{
            Enumeration<String> NameVars = session.getAttributeNames();

            String equation = "";

            HashMap<String, String> temp = new HashMap<>();

            while (NameVars.hasMoreElements()) {
                String key = NameVars.nextElement();
                String val = (String)session.getAttribute(key);
                if (key == "equation"){
                    equation = val;
                }else{
                    temp.put(key,val);
                }
            }

            equation = generateEquation(temp, equation).replaceAll(" ", "");

            //String equation = req.getParameter("equation").replace(" ", "");

            /*while (!Pattern.matches("^[0-9*+-/()]+$", equation)){
                for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()){
                    equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
                }
            }

            */
            if (equation == "error"){
                resp.setStatus(409);
            }else {
                out.print(evaluate(equation, out));
            }
        }

        out.flush();
        out.close();
    }

    private  String generateEquation(HashMap<String, String> temp, String equation){
        for (Map.Entry<String, String> entry: temp.entrySet()){
            equation = equation.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry: temp.entrySet()){
            equation = equation.replace(entry.getKey(), entry.getValue());
        }

        for (int i=0;i<equation.length();i++){
            String t = Character.toString(equation.charAt(i));
            if (Extensions.isSymbol(t)){
                if (temp.containsKey(t)){
                    return generateEquation(temp, equation);
                }else {
                    return "error";
                }
            }
        }

        return equation;
    }

    private Integer evaluate(String data, PrintWriter out ){

        String parsedData = data.replaceAll("-","+-");

        while (parsedData.contains("(")){
            int start = parsedData.lastIndexOf("(");
            int finish = parsedData.indexOf(")", start);

            String temp = parsedData.substring(start, finish+1);

            parsedData = parsedData.replace(temp, countMicro(temp.replace("(","").replace(")",""), out).toString());
        }

        return (countMicro(parsedData, out));
    }

    protected Integer countMicro(String data, PrintWriter out ){
        String temp = data;

        if (temp.charAt(0) == '+'){
            temp = temp.substring(1);
        }

        int currentPriority = 0;

        while (Extensions.containsOperation(temp)){
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
                }else if(Extensions.containsOperation(curChar)){
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

            if (!Extensions.equationContainsSome(temp, list)){
                currentPriority++;
            }

            if (currentPriority >= priorityList.length){
                if (Extensions.containsOperation(temp)){
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

        if (ts.contains("*")){
            val =  v1 * v2;
        }
        if (ts.contains("/")){
            val =  v1 / v2;
        }
        if (ts.contains("+")){
            val = v1 + v2;
        }

        return val;
    }
}
