import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

@WebServlet(
        name = "123",
        urlPatterns = ("/calc")
)
public class Solution extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        StringBuilder equationWithoutVars = new StringBuilder();
        for (int i = 0; i < equation.length(); ++i) {
            char cur = equation.charAt(i);
            if (cur >= 'a' && cur <= 'z') {
                String s = req.getParameter(String.valueOf(cur));
                while (s.charAt(0) >= 'a' && s.charAt(0) <= 'z') {
                    s = req.getParameter(s);
                }
                equationWithoutVars.append(s);
            } else {
                equationWithoutVars.append(cur);
            }
        }
        out.print(get(equationWithoutVars.toString()));
        out.flush();
        out.close();
    }

    private boolean isOperator(char c) {
        return c == '-' || c == '+' || c == '*' || c == '/';
    }

    private int priorityOfOperator(char c) {
        switch (c) {
            case '*':
            case '/':
                return 1;
            default:
                return 0;
        }
    }

    private int get(String in) {
        String equation = RPN(in);
        int left;
        int right;
        String s;
        Deque<Integer> st = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(equation);
        while (tokenizer.hasMoreTokens()) {
            s = tokenizer.nextToken().trim();
            if (1 == s.length() && isOperator(s.charAt(0))) {
                right = st.pop();
                left = st.pop();
                switch (s.charAt(0)) {
                    case '+':
                        left += right;
                        break;
                    case '-':
                        left -= right;
                        break;
                    case '/':
                        left /= right;
                        break;
                    case '*':
                        left *= right;
                        break;
                    default:
                        break;
                }
                st.push(left);
            } else {
                left = Integer.parseInt(s);
                st.push(left);
            }
        }
        return st.pop();
    }


    private String RPN(String s) {
        StringBuilder sbstack = new StringBuilder();
        StringBuilder sbOut = new StringBuilder();
        char cIn;
        char cTmp;
        for (int i = 0; i < s.length(); i++) {
            cIn = s.charAt(i);
            if (isOperator(cIn)) {
                while (sbstack.length() > 0) {
                    cTmp = sbstack.substring(sbstack.length() - 1).charAt(0);
                    if (isOperator(cTmp) && (priorityOfOperator(cIn) <= priorityOfOperator(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        sbstack.setLength(sbstack.length() - 1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbstack.append(cIn);
            } else if ('(' == cIn) {
                sbstack.append(cIn);
            } else if (')' == cIn) {
                cTmp = sbstack.substring(sbstack.length() - 1).charAt(0);
                while ('(' != cTmp) {
                    sbOut.append(" ").append(cTmp);
                    sbstack.setLength(sbstack.length() - 1);
                    cTmp = sbstack.substring(sbstack.length() - 1).charAt(0);
                }
                sbstack.setLength(sbstack.length() - 1);
            } else {
                sbOut.append(cIn);
            }
        }

        while (sbstack.length() > 0) {
            sbOut.append(" ").append(sbstack.substring(sbstack.length() - 1));
            sbstack.setLength(sbstack.length() - 1);
        }

        return sbOut.toString();
    }

}
