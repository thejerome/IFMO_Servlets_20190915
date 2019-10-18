import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.regex.Pattern;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc/result"}
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        HttpSession sess = req.getSession(false);
        if (sess.getAttribute("equation") == null) {
            resp.setStatus(409);
        }
        else if (sess != null) {
            HashMap<String, String> li = new HashMap<>();
            Enumeration<String> NameVars = sess.getAttributeNames();
            while (NameVars.hasMoreElements()) {
                String key = NameVars.nextElement();
                String val = (String)sess.getAttribute(key);
                li.put(key, val);
            }
            String equation = MakeEquation(li);
            if (Pattern.matches(".*[a-zA-Z].*", equation)) resp.setStatus(409);
            else out.print(calc(" " + equation));
            out.flush();
            out.close();
        }
    }

    private String MakeEquation(HashMap<String, String> li) {
        HashMap<String, String> vars = new HashMap<>();
        String equation = "";
        for (String keys : li.keySet()) {
            if (keys.equals("equation")) equation = li.get(keys);
            else if (vars.containsKey(li.get(keys))) vars.put(keys, vars.get(li.get(keys)));
            else vars.put(keys, li.get(keys));
        }
        equation = equation.replaceAll(" ", "");
        StringJoiner sj = new StringJoiner("");
        String[] s = equation.split("");
        for (int i=0; i<s.length; i++) {
            if (vars.containsKey(s[i])) s[i] = vars.get(s[i]);
            sj.add(s[i]);
        }
        return sj.toString();
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
                    if (str.charAt(endIndex) == '(') cnt++;
                    else if (str.charAt(endIndex) == ')') cnt--;
                    endIndex++;
                }
                subs = calc(str.substring(i,endIndex-1));
                j = endIndex;
            }
            else {
                j = i;
                while (j < str.length() && (isOperator(str.charAt(j)) || j==1)) j++;
                subs = str.substring(i, j);
            }
            if (j == str.length()) {
                if (operators.charAt(1) == ' ') tmpres = Integer.parseInt(subs);
                else tmpres = solveNPath(operators.charAt(1), tmpres, Integer.parseInt(subs));
                res = solveNPath(operators.charAt(0), res, tmpres);
                i=j;
                continue;
            }
            if (operators.charAt(1) == '*' || operators.charAt(1) == '/') {
                tmpres = solveNPath(operators.charAt(1), tmpres, Integer.parseInt(subs));
                if (str.charAt(j) == '+' || str.charAt(j) == '-') {
                    res = solveNPath(str.charAt(j), res, tmpres);
                    tmpres = 0;
                    operators = str.charAt(j) + " ";
                }
                else operators = operators.replace(operators.charAt(1),str.charAt(j));
            }
            else {
                if (str.charAt(j) == '*' || str.charAt(j) == '/') {
                    tmpres += Integer.parseInt(subs);
                    operators = operators.replace(operators.charAt(1),str.charAt(j));
                }
                else {
                    if (operators.charAt(0) == '+') res += Integer.parseInt(subs);
                    else res -= Integer.parseInt(subs);
                    operators = operators.replace(operators.charAt(0),str.charAt(j));

                }
            }
            i=j;
        }
        return String.valueOf(res);
    }

    private int solveNPath(char op, int op1, int op2) {
        int result = 0;
        switch (op) {
            case('+'):
                result = op1 + op2;
                break;
            case('-'):
                result = op1 - op2;
                break;
            case('*'):
                result = op1 * op2;
                break;
            case('/'):
                result = op1 / op2;
                break;
            default:
                break;
        }
        return result;
    }

    private boolean isOperator(char c) {
        return !(c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')');
    }
}