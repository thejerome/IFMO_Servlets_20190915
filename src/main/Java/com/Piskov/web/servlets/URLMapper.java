package com.Piskov.web.servlets;

import java.util.Map;

public class URLMapper {
    public static String Map(Map<String, String[]> variables, String equation){
        //equation = equation.replaceAll("\\s+", "");
        while (consistOfLetters(equation)){
            for (char symbol: equation.toCharArray()) {
                if (symbol >= 'a' && symbol <= 'z') {
                    equation = equation.replace(String.valueOf(symbol), String.valueOf(variables.get(String.valueOf(symbol))[0]));
                }
            }
        }
        return equation;
    }
    private static boolean consistOfLetters(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('a' <= expression.charAt(i) && 'z' >= expression.charAt(i)) return true;
        }
        return false;
    }

}

