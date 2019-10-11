import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc"}
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        String equation = req.getParameter("equation");
        equation = equation.replaceAll("\\s+", "");
        out.println(calculation(req, equation));
        out.flush();
        out.close();
    }

    public String calculation(HttpServletRequest req, String str) {
        int res = 0, tmpres = 0;
        String operators = "+ ";
        for (int i=0; i<str.length(); i++) {
            String subs;
            int j;
            if (i == '(') {
                int endIndex = str.lastIndexOf(')');
                subs = calculation(req, str.substring(i,endIndex));
                j = endIndex+1;
            }
            else {
                j = i;
                while (j < str.length() && !isOperator(str.charAt(j))) j++;
                subs = str.substring(i, j - 1);
            }
            if (!isNumber(subs)) getVar(req,subs);
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
                else operators.replace(operators.charAt(1),str.charAt(j));
            }
            else {
                if (str.charAt(j) == '*' || str.charAt(j) == '/') {
                    tmpres += StrtoNum(subs);
                    operators.replace(operators.charAt(1),str.charAt(j));
                }
                else {
                    if (str.charAt(j) == '+') res += StrtoNum(subs);
                    else res -= StrtoNum(subs);
                    operators.replace(operators.charAt(0),str.charAt(j));
                }
            }
            i=j;
        }
        return Integer.toString(res);
    }

    public boolean isNumber(String n) {
        for (int i=0; i< n.length(); i++)
            if (n.charAt(i) < '0' || n.charAt(i) > '9') return false;
        return true;
    }

    public boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') return true;
        return false;
    }

    public int StrtoNum(String stn) {
        int result = 0;
        for (int i=0; i<stn.length(); i++) {
            result = result*10 + Character.getNumericValue(stn.charAt(i));
        }
        return result;
    }

    public String getVar(HttpServletRequest requ, String var) {
        while (!isNumber(var)) var = requ.getParameter(var);
        return var;
    }
}

