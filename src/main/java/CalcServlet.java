
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

@WebServlet(
        name = "calc",
        urlPatterns = ("/calc")
)

public class CalcServlet extends HttpServlet {
    private int nomberOfRes = 0;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        HashMap<String, Integer> vertexes = new HashMap<>();
        Map<String,String[]> args = req.getParameterMap();
        for (String key : args.keySet()){
            if (!"equation".equals(key)){
                getValueOfVert(key,vertexes,req);
            }
        }
        //выолнить операции в скобках
        while (equation.indexOf('(') != -1){
            int leftparentheses = equation.lastIndexOf('(');
            int rightparentheses = equation.indexOf(')', leftparentheses);
            Pair<String,Integer> p = doAllOperations(leftparentheses,
                    rightparentheses,
                    equation,
                    vertexes,
                    req);
            equation = p.getKey();
            rightparentheses = equation.indexOf(')', leftparentheses);
            equation = equation.substring(0,leftparentheses) +
                    "res" + (nomberOfRes - 1) +
                    equation.substring(rightparentheses + 1);
            //out.println(equation);
        }
        //выполнить оставшиеся операции
        out.println(doAllOperations(0,
                equation.length(),
                equation,
                vertexes,
                req).getValue());
        //out.println(vertexes);
        nomberOfRes = 0;
        //int ans = ;
        //out.println(equation);
        //out.println(ans);
    }
    private Pair<Integer,Integer> findElem(int j,
                          String equation,
                          int turn){
        int nearElem = j + turn;
        while (equation.charAt(nearElem) == ' ') nearElem += turn;
        int furtherElem = nearElem;
        while ( furtherElem < equation.length() &&
                furtherElem >= 0 &&
                equation.charAt(furtherElem) != '*' &&
                equation.charAt(furtherElem) != '/' &&
                equation.charAt(furtherElem) != '+' &&
                equation.charAt(furtherElem) != '-' &&
                equation.charAt(furtherElem) != ')' &&
                equation.charAt(furtherElem) != '(' &&
                equation.charAt(furtherElem) != ' ') furtherElem += turn;
        return new Pair<>(nearElem, furtherElem);
    }
    private int getValueOfVert(String nameVert,
                               HashMap<String,Integer> vertexes,
                               HttpServletRequest req){
        int valueVert;
        if (vertexes.containsKey(nameVert)) {
            valueVert = vertexes.get(nameVert);
        }else{
            if (vertexes.containsKey(req.getParameter(nameVert))){
                valueVert = vertexes.get(req.getParameter(nameVert));
                vertexes.put(nameVert, valueVert);
            }else{
                valueVert = Integer.parseInt(req.getParameter(nameVert));
                vertexes.put(nameVert, valueVert);
            }
        }
        return valueVert;
    }
    private int getValueOfOperation(char operation,
                                    int valueLeftVert,
                                    int valueRightVert){
        int ValueOfOperation = 0;
        if (operation == '+') {
            ValueOfOperation = valueLeftVert + valueRightVert;
        }else if (operation == '-' ){
            ValueOfOperation = valueLeftVert - valueRightVert;
        }else if (operation == '*' ){
            ValueOfOperation = valueLeftVert * valueRightVert;
        }else if (operation == '/' ){
            ValueOfOperation = valueLeftVert / valueRightVert;
        }
        return ValueOfOperation;
    }
    private Pair<String,Boolean> doFirstOperations(int startIndex,
                              int endIndex,
                              int operationType,
                              String equ,
                              HashMap<String, Integer> vertexes,
                              HttpServletRequest req) {
        String equation = equ;
        int j = startIndex;
        if (operationType == 1) {
            while (j < endIndex &&
                    equation.charAt(j) != '*' &&
                    equation.charAt(j) != '/') j++;
        }else{
            while (j < endIndex &&
                    equation.charAt(j) != '+' &&
                    equation.charAt(j) != '-') j++;
        }
        boolean operationFind = false;
        if (j < endIndex) {
            operationFind = true;
            Pair<Integer, Integer> leftElem = findElem(j, equation, -1);
            int left = leftElem.getValue() + 1;
            int right = leftElem.getKey() + 1;
            int l = left;
            String nameLeftVert = equation.substring(left, right);
            int valueLeftVert = getValueOfVert(nameLeftVert,
                    vertexes,
                    req);
            Pair<Integer, Integer> rightElem = findElem(j, equation, 1);
            left = rightElem.getKey();
            right = rightElem.getValue();
            int r = right;
            String nameRightVert = equation.substring(left, right);
            int valueRightVert = getValueOfVert(nameRightVert,
                    vertexes,
                    req);
            String nameOfRes = "res" + nomberOfRes;
            int valueOfRes = getValueOfOperation(equation.charAt(j),
                    valueLeftVert,
                    valueRightVert);
            vertexes.put(nameOfRes, valueOfRes);
            if (r < equation.length()) {
                equation = equation.substring(0, l) +
                        nameOfRes +
                        equation.substring(r);
            }else{
                equation = equation.substring(0, l) +
                        nameOfRes;
            }
        }
        return new Pair<>(equation, operationFind);
    }
    private Pair<String, Integer> doAllOperations(int startIndex,
                                                  int endIndex,
                                                  String equ,
                                                  HashMap<String, Integer> vertexes,
                                                  HttpServletRequest req){
        String equation = equ;
        Pair<String,Boolean> p = new Pair<>("",true);
        while (p.getValue()){
            p = doFirstOperations(startIndex,
                    endIndex,
                    1,
                    equation,
                    vertexes,
                    req);
            if (p.getValue()) {
                equation = p.getKey();
                nomberOfRes++;
                endIndex = equation.indexOf(')', startIndex);
                if (endIndex == -1) endIndex = equation.length();
            }
        }
        p = new Pair<>("",true);
        while (p.getValue()) {
            p = doFirstOperations(startIndex,
                    endIndex,
                    2,
                    equation,
                    vertexes,
                    req);
            equation = p.getKey();
            if (p.getValue()) {
                nomberOfRes++;
                endIndex = equation.indexOf(')', startIndex);
                if (endIndex == -1) endIndex = equation.length();
            }
        }
        return new Pair<>(equation, vertexes.get("res" + (nomberOfRes - 1)));
    }
}
