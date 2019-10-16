import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.*;

@WebServlet(
        name = "Result",
        urlPatterns = {"/calc/result"}
)
public class Result extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        HttpSession sesh = req.getSession();
        Map<String, Object> params = (HashMap<String, Object>) sesh.getAttribute("vars");

        if (sesh.getAttribute("equation") == null) {
            resp.setStatus(409);
            out.write("Nothing to evaluate");
        }
        else {
            String equation = (String) sesh.getAttribute("equation");
            if (hasVars((String) sesh.getAttribute("equation")) == params.size()) {
                while (hasVars(equation) > 0) {
                    StringBuilder withValues = new StringBuilder();
                    for (int i = 0; i < equation.length(); i++) {
                        char elem = equation.charAt(i);
                        if (Character.isLetter(elem)) {
                            String result = params.get(Character.toString(elem)).toString();
                            withValues.append(result);
                        } else {
                            withValues.append(elem);
                        }
                    }
                    equation = withValues.toString();
                }
                ArrayDeque<String> rpn = shuntingYard(equation);
                out.print(calculate(rpn));
            }
            else {
                resp.setStatus(409);
            }
            out.flush();
            out.close();
        }
    }

    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm#The_algorithm_in_detail
    private ArrayDeque<String> shuntingYard(String infix) {
        ArrayDeque<String> postfix = new ArrayDeque<>();
        ArrayDeque<String> operStack = new ArrayDeque<>();
        StringBuilder temp = new StringBuilder();
        int lastOper = 0;
        boolean negative = false;
        char token;
        String top;

        for (int i = 0; i < infix.length(); i++) {
            token = infix.charAt(i);
            if (Character.isDigit(token)) {
                temp.append(token);
            }
            if (!Character.isDigit(token) || i == infix.length() - 1) {
                if (negative) {
                    postfix.offerLast("-" + temp.toString());
                    negative = false;
                }
                else {
                    postfix.offerLast(temp.toString());
                }
                temp.delete(0, temp.length());
            }

            if (isOper(token)) {
                if ((lastOper == i-1 || operStack.peekLast() == "(") && token == '-' || token == '-' && i == 0) {
                    negative = true;
                }
                else {
                    while (operStack.peekLast() != null && precedence(token) <= precedence(operStack.peekLast().charAt(0)) &&
                            !operStack.peekLast().equals("(")) {
                        postfix.offerLast(operStack.pollLast());
                    }
                    operStack.offerLast(Character.toString(token));
                    lastOper = i;
                }
            }

            if (token == '(') {
                operStack.offerLast(Character.toString(token));
            }

            if (token == ')') {
                top = operStack.peekLast();
                while (!"(".equals(top)) {
                    postfix.offerLast(operStack.pollLast());
                    top = operStack.peekLast();
                }
                operStack.pollLast();
            }
        }

        while (!operStack.isEmpty()) {
            postfix.offerLast(operStack.pollLast());
        }
        return postfix;
    }

    private int precedence(char oper) {
        if (oper == '/' || oper == '*') {
            return 2;
        }
        else {
            return 1;
        }
    }

    private boolean isOper(char input) {
        return input == '-' || input == '+' || input == '*' || input == '/';
    }

    private int calculate(ArrayDeque<String> input) {
        String elem;
        Integer operand1;
        Integer operand2;
        Integer value;
        ArrayDeque<Integer> result = new ArrayDeque<>();
        for (String s : input) {
            elem = s;
            if (elem.length() == 1 && isOper(elem.charAt(0))) {
                operand2 = result.pollLast();
                operand1 = result.pollLast();
                if ("+".equals(elem)) {
                    value = operand1 + operand2;
                    result.offerLast(value);
                }
                if ("-".equals(elem)) {
                    value = operand1 - operand2;
                    result.offerLast(value);
                }
                if ("*".equals(elem)) {
                    value = operand1 * operand2;
                    result.offerLast(value);
                }
                if ("/".equals(elem)) {
                    value = operand1 / operand2;
                    result.offerLast(value);
                }
            }
            else {
                if (!"".equals(elem)) {
                    result.offerLast(Integer.valueOf(elem));
                }
            }
        }
        // may produce npe but it won't
        return result.pollLast();
    }

    private int hasVars(String input) {
        String temp= "";

        for (int i = 0; i < input.length(); i++) {
            if(temp.indexOf(input.charAt(i)) == -1 && Character.isLetter(input.charAt(i))){
                temp = temp + input.charAt(i);
            }
        }
        return temp.length();
    }
}
