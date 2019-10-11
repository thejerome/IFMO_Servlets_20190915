package com.sam.web.servlets;

import java.util.Map;

public class VarInMap {
    public static String inMap(Map<String, String[]> var, String equation){

        while (isLetters(equation)) {
            for (int i = 0; i < equation.length(); i++) {
                char symbol = equation.charAt(i);
                if (symbol >= 'a' && symbol <= 'z') {
                    equation = equation.replace(String.valueOf(symbol), String.valueOf(var.get(String.valueOf(symbol))[0]));
                }
            }
        }
        return(equation);
    }

    private static boolean isLetters(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) return true;
        }
        return false;
    }


}