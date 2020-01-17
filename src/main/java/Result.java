import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@WebServlet(name = "Result", urlPatterns = {"/calc/result"})
public class Result extends HttpServlet {
    private static boolean isPriorOper(char character) {
        return character == '*' || character == '/';
    }

    private static boolean isSecondOper(char character) {
        return character == '+' || character == '-';
    }

    private static void operation(Stack<Integer> var, Stack<Character> oper) {
        int firstVar = var.pop();
        int secondVar = var.pop();
        switch (oper.pop()) {
            case '*':
                var.push(firstVar * secondVar);
                break;
            case '/':
                var.push(secondVar / firstVar);
                break;
            case '+':
                var.push(firstVar + secondVar);
                break;
            case '-':
                var.push(secondVar - firstVar);
                break;
            default:
                break;
        }
    }

    private int calc(String eq, Map<Character, Integer> map) {
        Stack<Character> stackOperators = new Stack<>();
        Stack<Integer> stackVars = new Stack<>();
        for (int i = 0; i < eq.length(); i++) {
            char eqChar = eq.charAt(i);
            if (Equation.isVariable(eqChar))
                stackVars.push(map.get(eqChar));
            else if (isSecondOper(eqChar)) {
                while ((stackVars.size() > 1) && (!stackOperators.empty()) && (isSecondOper(stackOperators.peek()) || isPriorOper(stackOperators.peek()))) {
                    operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);
            } else if (isPriorOper(eqChar)) {
                while ((stackVars.size() > 1) && (!stackOperators.empty()) && (isPriorOper(stackOperators.peek()))) {
                    operation(stackVars, stackOperators);
                }
                stackOperators.push(eqChar);
            } else if (eqChar == '(') {
                stackOperators.push(eqChar);
            } else if (eqChar == ')') {
                while (stackVars.size() > 1 && stackOperators.peek() != '(') {
                    operation(stackVars, stackOperators);
                }
                stackOperators.pop();
            } else {
                stackVars.push(Integer.parseInt(String.valueOf(eqChar)));
            }
        }

        while (!stackOperators.isEmpty() && stackVars.size() > 1)
            operation(stackVars, stackOperators);

        return stackVars.pop();
    }

    private boolean isAllVarsDefined(String equation, HttpSession httpSession) {
        for (int i = 0; i < equation.length(); ++i) {
            char c = equation.charAt(i);
            if (Equation.isVariable(c)) {
                String var = String.valueOf(c);
                if (httpSession.getAttribute(var) == null)
                    return false;
            }
        }
        return true;
    }

    private int result(String eq, HttpSession session) {
        Map<String, String> attributes = new HashMap<>();
        Enumeration<String> en = session.getAttributeNames();

        while (en.hasMoreElements()) {
            String el = en.nextElement();
            attributes.put(el, session.getAttribute(el).toString());
        }

        Map<Character, Integer> map = new HashMap<>();
        for (Map.Entry<String, String> attributesEntry : attributes.entrySet()) {
            if (!(attributesEntry.getKey().equals("equation"))) {
                String val = attributesEntry.getValue();
                if (Equation.isVariable(val.charAt(0))) {
                    for (Map.Entry<String, String> attrEntry : attributes.entrySet()) {
                        if (val.equals(attrEntry.getKey())) {
                            val = attrEntry.getValue();
                        }
                    }
                }
                int valInt = Integer.parseInt(val);
                map.put(attributesEntry.getKey().charAt(0), valInt);
            }
        }

        return calc(eq, map);
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession();
        PrintWriter writer = httpServletResponse.getWriter();
        if (session == null) {
            writer.println("session is null");
            httpServletResponse.setStatus(409);
        } else {
            String equation = (String) session.getAttribute("equation");
            if (equation == null) {
                writer.println("equation is null");
                httpServletResponse.setStatus(409);
            } else {
                if (isAllVarsDefined(equation, session)) {
                    writer.print(result(equation.replaceAll("\\s", ""), session));
                    httpServletResponse.setStatus(200);
                } else {
                    writer.println("some var is undefined");
                    httpServletResponse.setStatus(409);
                }
            }
        }
        writer.close();
    }

}

