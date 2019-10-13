import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writ = resp.getWriter();
        String equation = req.getParameter("equation");
        equation = equation.replaceAll("\\s+", "");
        equation = " " + equation;
        Map<String, String[]> equationMap = req.getParameterMap();
        for (int i=1; i<equation.length(); i++) {
            if (isChar(equation.charAt(i))) {
                String sub = equationMap.get(Character.toString(equation.charAt(i)))[0];
                while (!isNumber(sub)) sub = equationMap.get(sub)[0];
                if (isNumber(Character.toString(equation.charAt(i-1)))) sub = "*" + sub;
                if (i<equation.length()-1 && isNumber(Character.toString(equation.charAt(i+1)))) sub = sub + "*";
                equation = equation.replaceAll(Character.toString(equation.charAt(i)), sub);
            }
        }
        writ.write(calc(equation));
        writ.flush();
        writ.close();
    }

    private String calc(String str) {
        int res = 0;
        int tmpres = 0;
        String operators = "+ ";
        for (int i=1; i<str.length(); i++) {
            String subs;
            int j;
            if (str.charAt(i) == '(') {
                int endIndex = i+1;
                int cnt = 1;
                while (cnt != 0) {
                    switch (str.charAt(endIndex)) {
                        case('('):
                            cnt++;
                            break;
                        case(')'):
                            cnt--;
                            break;
                        default:
                            break;
                    }
                    endIndex++;
                }
                subs = calc(str.substring(i,endIndex-1));
                j = endIndex;
            }
            else {
                j = i;
                while (j < str.length() && isOperator(str.charAt(j))) j++;
                subs = str.substring(i, j);
            }
            if (j == str.length()) {
                switch (operators.charAt(1)) {
                    case ('*'):
                        tmpres *= StrtoNum(subs);
                        break;
                    case ('/'):
                        tmpres /= StrtoNum(subs);
                        break;
                    case (' '):
                        tmpres = StrtoNum(subs);
                        break;
                    default:
                        break;
                }
                switch (operators.charAt(0)) {
                    case('+'):
                        res += tmpres;
                        break;
                    case('-'):
                        res -= tmpres;
                        break;
                    case (' '):
                        break;
                    default:
                        break;
                }
                i=j;
                continue;
            }
            if (operators.charAt(1) == '*' || operators.charAt(1) == '/') {
                switch (operators.charAt(1)) {
                    case('*'):
                        tmpres *= StrtoNum(subs);
                        break;
                    case('/'):
                        tmpres /= StrtoNum(subs);
                        break;
                    default:
                        break;
                }
                if (str.charAt(j) == '+' || str.charAt(j) == '-') {
                    switch (str.charAt(j)) {
                        case('+'):
                            res += tmpres;
                            break;
                        case('-'):
                            res -= tmpres;
                            break;
                        default:
                            break;
                    }
                    tmpres = 0;
                    operators = str.charAt(j) + " ";
                }
                else operators = operators.replace(operators.charAt(1),str.charAt(j));
            }
            else {
                if (str.charAt(j) == '*' || str.charAt(j) == '/') {
                    tmpres += StrtoNum(subs);
                    operators = operators.replace(operators.charAt(1),str.charAt(j));
                }
                else {
                    if (operators.charAt(0) == '+') res += StrtoNum(subs);
                    else res -= StrtoNum(subs);
                    operators = operators.replace(operators.charAt(0),str.charAt(j));

                }
            }
            i=j;
        }
        return String.valueOf(res);
    }

    private boolean isNumber(String n) {
        for (int i=0; i< n.length(); i++)
            if (n.charAt(i) < '0' || n.charAt(i) > '9') return false;
        return true;
    }

    private boolean isOperator(char c) {
        return !(c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')');
    }

    private int StrtoNum(String stn) {
        int result = 0;
        for (int i=0; i<stn.length(); i++) {
            result = result*10 + Character.getNumericValue(stn.charAt(i));
        }
        return result;
    }

    private boolean isChar(char ch) {
        ch = Character.toLowerCase(ch);
        return !(ch < 'a' || ch > 'z');
    }
}
