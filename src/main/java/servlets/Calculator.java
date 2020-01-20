package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;


@WebServlet(
        name = "calculator",
        urlPatterns = {"/calc"}
)

public class Calculator extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        StringBuffer equationWtk = new StringBuffer();
        for (int i = 0; i < equation.length(); ++i) {
            if (test(String.valueOf(equation.charAt(i)))) {
                String parameterLetter = req.getParameter(String.valueOf(equation.charAt(i)));
                if (test(parameterLetter)) {
                    parameterLetter = req.getParameter(parameterLetter);
                }
                equationWtk.append(parameterLetter);
            } else {
                equationWtk.append(equation.charAt(i));
            }
        }
        out.print(get(equationWtk.toString()));
        out.flush();
        out.close();
    }

    private static boolean test(String test) {
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(test);
        return m.matches();
    }

    private int get(String result){
        String equation = preparation(result);
        StringBuffer current = new StringBuffer();
        ArrayList<Integer> Value = new ArrayList<>();
        int end = 0;
        for(int i = 0; i < equation.length();i++){
            if(whatIsOperator(equation.charAt(i))){
                switch (equation.charAt(i)) {
                    case '+':
                        if(Value.size() >= 2){
                            end = Value.get(1) + Value.get(0);
                            Value.remove(0);
                            Value.remove(0);
                            Value.add(0, end);
                        }
                        break;
                    case '-':
                        if(Value.size() >= 2){
                            end = Value.get(1) - Value.get(0);
                            Value.remove(0);
                            Value.remove(0);
                            Value.add(0, end);
                        }
                        break;
                    case '/':
                        if(Value.size() >= 2){
                            end = Value.get(1) / Value.get(0);
                            Value.remove(0);
                            Value.remove(0);
                            Value.add(0, end);
                        }
                        break;
                    case '*':
                        if(Value.size() >= 2){
                            end = Value.get(1) * Value.get(0);
                            Value.remove(0);
                            Value.remove(0);
                            Value.add(0, end);
                        }
                        break;
                    default:
                        break;
                }
            }else if(equation.charAt(i) != ' '){
                current.append(equation.charAt(i));
                if(equation.charAt(i+1) == ' ') {
                    Value.add(0, Integer.parseInt(String.valueOf(current)));
                }
            }else {
                current.replace(0, current.capacity(), "");
            }
        }
        return end;
    }
    
    private boolean whatIsOperator(char ch) {
        return ch == '*' || ch == '-' || ch == '/' || ch == '+' ;
    }

    private int isThatDorM(char ch) {
        switch (ch) {
            case '/':
            case '*':
                return 1;
            default:
                return 0;
        }
    }
    
    private String preparation(String transmittedString) {
        StringBuffer basket = new StringBuffer();
        StringBuffer finalOut = new StringBuffer();   
        char currentChar;
        char basketChar;
        for (int i = 0; i < transmittedString.length(); i++) {
            currentChar = transmittedString.charAt(i);
            if (whatIsOperator(currentChar)) {
                while (basket.length() > 0) {
                    basketChar = basket.charAt(basket.length() - 1);
                    if ((isThatDorM(currentChar) <= isThatDorM(basketChar)) && whatIsOperator(basketChar)) {
                        finalOut.append(" ").append(basketChar);
                        basket.setLength(basket.length() - 1);
                    } else {
                        finalOut.append(" ");
                        break;
                    }
                }
                finalOut.append(" ");
                basket.append(currentChar);
            } else if ('(' == currentChar) {
                basket.append(currentChar);
            } else if (')' == currentChar) {
                basketChar = basket.charAt(basket.length() - 1);
                while ('(' != basketChar) {
                    finalOut.append(" ").append(basketChar);
                    basket.setLength(basket.length() - 1);
                    basketChar = basket.charAt(basket.length() - 1);
                }
                basket.setLength(basket.length() - 1);
            } else {
                finalOut.append(currentChar);
            }
        }

        while (basket.length() > 0) {
            finalOut.append(" ").append(basket.charAt(basket.length() - 1));
            basket.setLength(basket.length() - 1);
        }

        return finalOut.toString();
    }
}