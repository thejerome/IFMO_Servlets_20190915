package util;

public class Check {

    public static boolean isEquationGood(String s) {
        for (int i = 0; i < s.length()-1; i++) {
            char current = s.charAt(i);
            char next = s.charAt(i+1);
            if (Character.isLetter(current) && Character.isLetter(next)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVarGood(String s) {
        if (s.length() == 1 && s.charAt(0) >= 'a' && s.charAt(0) <= 'z') {
            return true;
        }
        try {
            int a = Integer.parseInt(s);
            return a >= -10000 && a <= 10000;
        } catch (Exception e) {
            return false;
        }
    }
}



