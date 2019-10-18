package instruments;

public class CheckForProblems {
    public static boolean equationCheck(String expr){
        for (int i = 0; i < expr.length() - 1; i++)
            if('a' <= expr.charAt(i) && expr.charAt(i) <= 'z' && 'a' <= expr.charAt(i+1) && expr.charAt(i+1) <= 'z')
                return true;
            return false;
    }

    public static boolean variableCheck(String expr){
        if (expr.charAt(0) >= 'a' && expr.charAt(0) <= 'z' && expr.length() == 1) return true;
        try {
            int num = Integer.parseInt(expr);
            return num <= 10000 && num >= -10000;
        }
        catch (Exception ex){ return false;}
    }
}
