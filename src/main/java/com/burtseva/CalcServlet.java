package com.burtseva;

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

    private int calc(String str){
        int firstBracket;
        int secondBracket;
        int resultOfIncludedCalc;
        char sign;
        String stringForFutureResult;
        String tempString;
        StringBuilder stringBuilder = new StringBuilder(str);
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

        stringForFutureResult = stringBuilder.toString();

        while (stringForFutureResult.contains("*") || stringForFutureResult.contains("/")){
            sign = checkMultiplicationAndDivision(stringForFutureResult);
            stringForFutureResult = change(stringForFutureResult, sign);
        }

        while ( (stringForFutureResult.charAt(0) != '-') && (stringForFutureResult.contains("+") || stringForFutureResult.contains("-")) ){
            sign = checkPlusAndMinus(stringForFutureResult);
            stringForFutureResult = change(stringForFutureResult, sign);
        }

        return Integer.parseInt(stringForFutureResult);
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


    private char checkMultiplicationAndDivision(String str){
        if (str.contains("*") && str.contains("/")){
            if (str.indexOf("*") < str.indexOf("/"))
                return '*';
            if (str.indexOf("/") < str.indexOf("*"))
                return '/';
        }
        else if (str.contains("/"))
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

    private String change (String str, char sign){
        int signLocation;
        int firstNumLocation;
        int secondNumLocation;
        int firstNum;
        int secondNum;
        int calcResult;
        String tempString;
        StringBuilder stringBuilderForFirstNum = new StringBuilder();
        StringBuilder stringBuilderForSecondNum = new StringBuilder();

        signLocation = str.indexOf(String.valueOf(sign));
        firstNumLocation = signLocation - 1;

        while ((firstNumLocation > -1) && (!checkingForCorrectSign(str.charAt(firstNumLocation)))){
            stringBuilderForFirstNum.append(str.charAt(firstNumLocation));
            firstNumLocation -= 1;
        }
        stringBuilderForFirstNum.reverse();

        if (firstNumLocation > -1){

            if((firstNumLocation == 0)&&(str.charAt(firstNumLocation) == '-')){
                stringBuilderForFirstNum.insert(0,'-');
                firstNumLocation--;
            }
            else if (firstNumLocation != 0){
                tempString = str.substring(firstNumLocation-1,firstNumLocation+1);
                if ( (tempString.charAt(1) == '-') && (checkingForCorrectSign(tempString.charAt(0))) ) {
                    stringBuilderForFirstNum.insert(0,'-');
                    firstNumLocation--;
                }
            }

        }

        firstNum = Integer.parseInt(stringBuilderForFirstNum.toString());

        secondNumLocation = signLocation + 1;
        if (str.charAt(secondNumLocation) == '-') {
            stringBuilderForSecondNum.append('-');
            secondNumLocation++;
        }

        while ( (secondNumLocation < str.length()) && (!checkingForCorrectSign(str.charAt(secondNumLocation))) ){
            stringBuilderForSecondNum.append(str.charAt(secondNumLocation));
            secondNumLocation++;
        }

        secondNum = Integer.parseInt(stringBuilderForSecondNum.toString());

        stringBuilderForFirstNum.setLength(0);
        stringBuilderForSecondNum.setLength(0);

        calcResult = count(firstNum,secondNum,sign);

        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.delete(firstNumLocation + 1,secondNumLocation);
        stringBuilder.insert(firstNumLocation + 1,calcResult);

        return stringBuilder.toString();
    }
}
