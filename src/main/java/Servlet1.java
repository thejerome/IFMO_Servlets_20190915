import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet (
        name = "Servlet1",
        urlPatterns = {"/calc"}
)
public class Servlet1 extends HttpServlet{

    private static boolean isPriorityOperator (char oper) {
        return oper == '*' || oper == '/';
    }
    private static boolean isSecondaryOperator (char oper) {
        return oper == '+' || oper == '-';
    }
    private static boolean isVariable (char oper) {
        return oper >= 'a' && oper <= 'z';
    }
    private static void Operation (Stack<Integer> var, Stack<Character> oper) {
        int var1 = var.pop();
        int var2 = var.pop();
        switch (oper.pop()) {
            case '+':
                var.push(var1+var2);
                break;
            case '-':
                var.push(var2-var1);
                break;
            case '*':
                var.push(var1*var2);
                break;
            case '/':
                var.push(var2/var1);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getParameter("equation");
        PrintWriter writer = resp.getWriter();
        Map<Character, Integer> normalMap = new HashMap<>();
        final Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            if (!(parameterEntry.getKey().equals("equation"))) {
                String val = parameterEntry.getValue()[0];
                if (isVariable(val.charAt(0))) {
                    for (Map.Entry<String, String[]> parEntry : parameterMap.entrySet()) {
                        if (val.equals(parEntry.getKey())) {
                            val = parEntry.getValue()[0];
                        }
                    }
                }
                int valInt = Integer.parseInt(val);
                normalMap.put(parameterEntry.getKey().charAt(0), valInt);
                }
        }

        Stack<Character> stackOperators = new Stack<>();
        Stack<Integer> stackVars = new Stack<>();
        for (int i = 0; i < equation.length(); i++) {
            char eqChar = equation.charAt(i);
            if (isVariable(eqChar))
                stackVars.push(normalMap.get(eqChar));
            else if (isSecondaryOperator(eqChar)) {
                while ((!stackOperators.empty()) && (isSecondaryOperator(stackOperators.peek()) || isPriorityOperator(stackOperators.peek()))) {
                    Operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);
            }
            else if (isPriorityOperator(eqChar)) {
                while ((!stackOperators.empty()) && (isPriorityOperator(stackOperators.peek()))) {
                    Operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);
            }
            else if (eqChar == '(') {
                stackOperators.push(eqChar);
            }
            else if (eqChar == ')') {
                while (stackOperators.peek() != '(') {
                    Operation(stackVars, stackOperators);
                }
                stackOperators.pop();
            }
            if (i == equation.length() - 1) {
                while (!stackOperators.isEmpty())
                    Operation(stackVars, stackOperators);
            }
        }

        writer.println(stackVars.pop());
        writer.flush();
        writer.close();
    }
}
