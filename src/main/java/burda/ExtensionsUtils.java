package burda;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ExtensionsUtils {
    public static boolean containsOperation(String data){
        return data.contains("+") || data.contains("/") || data.contains("*");
    }

    public static boolean isNumeric(String data){
        return data.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isSymbol(String data){
        return data.charAt(0) >= 'a' && data.charAt(0) <= 'z';
    }

    public static boolean checkVariable(String data, int min, int max){
        if (data.charAt(0) >= 'a' && data.charAt(0) <= 'z') {
            return true;
        }

        if (ExtensionsUtils.isNumeric(data)){
            int parsed = Integer.parseInt(data);
            return parsed > min && parsed < max;
        }
        return false;
    }

    public static boolean containsUnknown(String data){
        for (int i = 0; i < data.length(); i++){
            if(data.charAt(i)>='A' && data.charAt(i)<='Z') {
                return true;
            }
        }
        return false;
    }

    public static String getVariableKeyFromURI(HttpServletRequest req){
        String uri = req.getRequestURI();
        return String.valueOf(uri.charAt(uri.length()-1));
    }

    public static boolean equationContainsSome(String data, List<String> checkData){
        for (int i = 0; i < data.length(); i++){
            if (checkData.contains(Character.toString(data.charAt(i)))){
                return true;
            }
        }
        return false;
    }
}