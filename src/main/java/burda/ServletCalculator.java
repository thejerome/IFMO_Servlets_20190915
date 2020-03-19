package burda;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "ServletCalculator",
        urlPatterns = {"/calc/result"}
)
public class ServletCalculator extends HttpServlet {

    private String priorityList [][] = {{"*","/"}, {"+"}};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

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
                if ("equation".equals(key)){
                    equation = val;
                }else{
                    temp.put(key,val);
                }
            }

            equation = generateEquation(temp, equation).replaceAll(" ", "");

            if ("error".equals(equation)){
                resp.setStatus(409);
            }else {
                out.print(evaluate(equation));
            }
        }
    }

    private  String generateEquation(HashMap<String, String> temp, String data){

        String equation = data;

        for (Map.Entry<String, String> entry: temp.entrySet()){
            equation = equation.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry: temp.entrySet()){
            equation = equation.replace(entry.getKey(), entry.getValue());
        }

        for (int i = 0; i < equation.length(); i++){
            String t = Character.toString(equation.charAt(i));
            if (ExtensionsUtils.isSymbol(t)){
                if (temp.containsKey(t)){
                    return generateEquation(temp, equation);
                }else {
                    return "error";
                }
            }
        }

        return equation;
    }

    private Integer evaluate(String data){

        String parsedData = data.replaceAll("-","+-");

        while (parsedData.contains("(")){
            int start = parsedData.lastIndexOf("(");
            int finish = parsedData.indexOf(")", start);

            String temp = parsedData.substring(start, finish+1);

            parsedData = parsedData.replace(temp, countMicro(temp.replace("(","").replace(")","")).toString());
        }

        return (countMicro(parsedData));
    }

    protected String fixFirstPlus(String data){
        String temp = data;

        if (temp.charAt(0) == '+'){
            temp = temp.substring(1);
        }

        return temp;
    }

    protected Integer countMicro(String data){
        String temp = fixFirstPlus(data);

        int currentPriority = 0;

        while (ExtensionsUtils.containsOperation(temp)){
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
                }else if(ExtensionsUtils.containsOperation(curChar)){
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

            if (!ExtensionsUtils.equationContainsSome(temp, list)){
                currentPriority++;
            }

            if (currentPriority >= priorityList.length){
                if (ExtensionsUtils.containsOperation(temp)){
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

        if (ts.contains("*"))val =  v1 * v2;
        if (ts.contains("/"))val =  v1 / v2;
        if (ts.contains("+"))val = v1 + v2;

        return val;
    }
}