import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "CalcServlet", urlPatterns = {"/calc"}
)
public class CalcServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        Map<String, String[]> parameterMap= request.getParameterMap();

        String equation = request.getParameter("equation").replace(" ", "");
        while (!equation.matches("^[0-9*+-/()]+$")) {
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }

        printWriter.println(calc(equation));
        printWriter.close();
    }

    private int calc(String string){
        int firstBracket;
        int secondBracket;
        int resultOfIncludedCalc;
        char sign;
        String tempString;
        StringBuilder stringBuilder = new StringBuilder(string);
        StringBuilder tempStringBuilder = new StringBuilder();

        while (stringBuilder.toString().contains("(")){

            firstBracket = stringBuilder.indexOf("(");
            secondBracket = stringBuilder.indexOf(")");
            tempStringBuilder.append(stringBuilder.substring(firstBracket+1,secondBracket));

            if (tempStringBuilder.toString().contains("(")){
                tempString = tempStringBuilder.substring(tempStringBuilder.indexOf("(") + 1);
                tempStringBuilder.setLength(0);
                tempStringBuilder.append(tempString);
                firstBracket = secondBracket - tempStringBuilder.length() - 1;
            }
            resultOfIncludedCalc = calc(tempStringBuilder.toString());
            tempStringBuilder.setLength(0);

            stringBuilder.delete(firstBracket,secondBracket + 1);
            stringBuilder.insert(firstBracket,resultOfIncludedCalc);
        }

        string = stringBuilder.toString();

        while (string.contains("*") || string.contains("/")){
            sign = checkMultiplicationAndDivision(string);
            string = change(string, sign);
        }

        while ( (string.charAt(0) != '-') && (string.contains("+") || string.contains("-")) ){
            sign = checkPlusAndMinus(string);
            string = change(string, sign);
        }

        return Integer.parseInt(string);
    }

    private boolean checkingForCorrectSign(char c){
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private char checkPlusAndMinus(String s){
        if ( s.contains("+") && s.contains("-") ){
            if (s.indexOf("-") < s.indexOf("+"))
                return '-';
            if (s.indexOf("+") < s.indexOf("-"))
                return '+';
        }
        else if (s.contains("-"))
            return '-';
        return '+';
    }


    private char checkMultiplicationAndDivision(String string){
        if (string.contains("*") && string.contains("/")){
            if (string.indexOf("*") < string.indexOf("/"))
                return '*';
            if (string.indexOf("/") < string.indexOf("*"))
                return '/';
        }
        else if (string.contains("/"))
            return '/';
        return '*';
    }

    private int count(int firstNum, int secondNum, char sign){
        switch(sign){
            case '*':
                return firstNum * secondNum;
            case '/':
                return firstNum / secondNum;
            case '+':
                return firstNum + secondNum;
            case '-':
                return firstNum - secondNum;
            default:
                break;
        }
        return 0;
    }

    private String change (String string, char sign){
        int signLocation;
        int firstNumLocation;
        int secondNumLocation;
        int firstNum;
        int secondNum;
        int calcResult;
        String tempString;
        StringBuilder stringBuilderForFirstNum = new StringBuilder();
        StringBuilder stringBuilderForSecondNum = new StringBuilder();

        signLocation = string.indexOf(String.valueOf(sign));
        firstNumLocation = signLocation - 1;

        while ((firstNumLocation > -1) && (!checkingForCorrectSign(string.charAt(firstNumLocation)))){
            stringBuilderForFirstNum.append(string.charAt(firstNumLocation));
            firstNumLocation -= 1;
        }
        stringBuilderForFirstNum.reverse();

        if (firstNumLocation > -1){

            if((firstNumLocation == 0)&&(string.charAt(firstNumLocation) == '-')){
                stringBuilderForFirstNum.insert(0,'-');
                firstNumLocation--;
            }
            else if (firstNumLocation != 0){
                tempString = string.substring(firstNumLocation-1,firstNumLocation+1);
                if ( (tempString.charAt(1) == '-') && (checkingForCorrectSign(tempString.charAt(0))) ) {
                    stringBuilderForFirstNum.insert(0,'-');
                    firstNumLocation--;
                }
            }

        }

        firstNum = Integer.parseInt(stringBuilderForFirstNum.toString());

        secondNumLocation = signLocation + 1;
        if (string.charAt(secondNumLocation) == '-') {
            stringBuilderForSecondNum.append('-');
            secondNumLocation++;
        }

        while ( (secondNumLocation < string.length()) && (!checkingForCorrectSign(string.charAt(secondNumLocation))) ){
            stringBuilderForSecondNum.append(string.charAt(secondNumLocation));
            secondNumLocation++;
        }

        secondNum = Integer.parseInt(stringBuilderForSecondNum.toString());

        stringBuilderForFirstNum.setLength(0);
        stringBuilderForSecondNum.setLength(0);

        calcResult = count(firstNum,secondNum,sign);

        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.delete(firstNumLocation + 1,secondNumLocation);
        stringBuilder.insert(firstNumLocation + 1,calcResult);

        return stringBuilder.toString();
    }
}
