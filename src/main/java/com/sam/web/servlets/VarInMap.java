package com.sam.web.servlets;

import java.util.Map;

public class VarInMap {
    public static String inMap(Map<String, String[]> var, String equation){
        String eq = equation;
        while (isLetters(eq)) {
            for (int i = 0; i < eq.length(); i++) {
                char symbol = eq.charAt(i);
                if (symbol >= 'a' && symbol <= 'z') {
                    eq = eq.replace(String.valueOf(symbol), String.valueOf(var.get(String.valueOf(symbol))[0]));
                }
            }
        }
        return(eq);
    }

    private static boolean isLetters(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) return true;
        }
        return false;
    }


}