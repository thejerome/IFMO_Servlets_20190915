import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        equation = equation.replaceAll("\\s+", "");
        equation = " " + equation;
        for (int i=0; i<equation.length(); i++) {
            if (isOperator(equation.charAt(i))) {
                int j = i;
                String subs1, subs2="";
                while (j < equation.length() && isOperator(equation.charAt(j))) j++;
                subs1 = equation.substring(i, j);
                if (!isNumber(subs1)) subs2 = req.getParameter(subs1);
                equation = equation.replaceAll(subs1, subs2);
                i=j;
            }
        }
        out.write(calculation(equation));
        out.flush();
        out.close();
    }

    private String calculation(String str) {
        int res = 0, tmpres = 0;
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
                subs = calculation(str.substring(i,endIndex-1));
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
                }
                if (str.charAt(j) == '+' || str.charAt(j) == '-') {
                    switch (str.charAt(j)) {
                        case('+'):
                            res += tmpres;
                            break;
                        case('-'):
                            res -= tmpres;
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
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') return false;
        return true;
    }

    private int StrtoNum(String stn) {
        int result = 0;
        for (int i=0; i<stn.length(); i++) {
            result = result*10 + Character.getNumericValue(stn.charAt(i));
        }
        return result;
    }
}
