import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "Solution",
        urlPatterns = {"/calc"}
)
public class Solution extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        String equation = req.getParameter("equation");
        Map<String, String[]> parameterMap = req.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < equation.length(); ++i) {
            char cur = equation.charAt(i);
            if (cur >= 'a' && cur <= 'z') {
                String newVal = parameterMap.get(String.valueOf(cur))[0];
                while ( !isDigits(newVal) ){
                    newVal =  parameterMap.get(newVal)[0];
                }
                sb.append(newVal);
            } else {
                sb.append(equation.charAt(i));
            }
        }
        equation = sb.toString();
        out.print(calculate(equation));
        out.flush();
        out.close();
    }

    private boolean isDigits(String input){
        for (int i = 0; i<input.length(); ++i){
            if (input.charAt(i) < '0' || input.charAt(i) > '9'){
                return false;
            }
        }
        return true;
    }

    private String toOPN(String input) {
        StringBuilder sbStack = new StringBuilder(), sbOut = new StringBuilder();
        char cIn, cTmp;
        for (int i = 0; i < input.length(); i++) {
            cIn = input.charAt(i);
            if (isOperator(cIn)) {
                while (sbStack.length() > 0) {
                    cTmp = sbStack.substring(sbStack.length() - 1).charAt(0);
                    if (isOperator(cTmp) && (priorityOfOperator(cIn) <= priorityOfOperator(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        sbStack.setLength(sbStack.length() - 1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbStack.append(cIn);
            } else if ('(' == cIn) {
                sbStack.append(cIn);
            } else if (')' == cIn) {
                cTmp = sbStack.substring(sbStack.length() - 1).charAt(0);
                while ('(' != cTmp) {
                    sbOut.append(" ").append(cTmp);
                    sbStack.setLength(sbStack.length() - 1);
                    cTmp = sbStack.substring(sbStack.length() - 1).charAt(0);
                }
                sbStack.setLength(sbStack.length() - 1);
            } else {
                sbOut.append(cIn);
            }
        }

        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length() - 1));
            sbStack.setLength(sbStack.length() - 1);
        }

        return sbOut.toString();
    }

    private boolean isOperator(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
                return true;
        }
        return false;
    }

    private int priorityOfOperator(char c) {
        switch (c) {
            case '*':
            case '/':
                return 2;
        }
        return 1;
    }

    private int calculate(String input) {
        input = toOPN(input);
        int lhs, rhs;
        String tmp;
        Deque<Integer> st = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            tmp = tokenizer.nextToken().trim();
            if (1 == tmp.length() && isOperator(tmp.charAt(0))) {
                rhs = st.pop();
                lhs = st.pop();
                switch (tmp.charAt(0)) {
                    case '+':
                        lhs += rhs;
                        break;
                    case '-':
                        lhs -= rhs;
                        break;
                    case '/':
                        lhs /= rhs;
                        break;
                    case '*':
                        lhs *= rhs;
                        break;
                }
                st.push(lhs);
            } else {
                lhs = Integer.parseInt(tmp);
                st.push(lhs);
            }
        }
        return st.pop();
    }
}