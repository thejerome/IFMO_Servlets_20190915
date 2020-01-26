package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import helpers.SomeHelper;

@WebServlet(
        name = "ResultServlet",
        urlPatterns = "/calc/result"
)
public class ResultServlet extends HttpServlet {

    public static String toPostfix(String infix) {
        char temp;
        StringBuilder result = new StringBuilder();
        StringBuilder hlp = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            if ('(' == infix.charAt(i)) {
                hlp.append(infix.charAt(i));
            } else if (')' == infix.charAt(i)) {
                temp = hlp.substring(hlp.length() - 1).charAt(0);
                while ('(' != temp) {
                    result.append(" ").append(temp);
                    hlp.setLength(hlp.length() - 1);
                    temp = hlp.substring(hlp.length() - 1).charAt(0);
                }
                hlp.setLength(hlp.length() - 1);
            } else if (SomeHelper.isOperator(infix.charAt(i))) {
                while (hlp.length() != 0) {
                    temp = hlp.substring(hlp.length() - 1).charAt(0);
                    if (SomeHelper.isOperator(temp) && (SomeHelper.precedence(infix.charAt(i)) <= SomeHelper.precedence(temp))) {
                        result.append(" " + temp + " ");
                        hlp.setLength(hlp.length() - 1);
                    } else {
                        break;
                    }
                }
                result.append(" ");
                hlp.append(infix.charAt(i));
            } else
                result.append(infix.charAt(i));
        }
        while (hlp.length() != 0) {
            result.append(" " + hlp.substring(hlp.length() - 1));
            hlp.setLength(hlp.length() - 1);
        }

        if (infix.charAt(0) == '-') {
            int index = result.lastIndexOf("-");
            result.deleteCharAt(index).deleteCharAt(index-1);
            result.insert(1,'-');
        }
        System.out.println("!!!!" + result.toString() + "!!!!");
        return result.toString();
    }

    public static int calculation(String st) {
        int left, right;
        Stack<Integer> stack = new Stack<>();
        String equation = toPostfix(st);
        StringTokenizer stringTokenizer = new StringTokenizer(equation);
        String tmp;
        while (stringTokenizer.hasMoreTokens()) {
            tmp = stringTokenizer.nextToken();
            if (SomeHelper.isOperator(tmp.charAt(0)) && 1 == tmp.length()) {
                right = stack.pop();
                left = stack.pop();
                left = SomeHelper.calculate(left, right, tmp.charAt(0));
                stack.push(left);
            } else {
                left = Integer.parseInt(tmp);
                stack.push(left);
            }
        }
        return stack.pop();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        PrintWriter out = response.getWriter();
        Map<String, String> map = new HashMap<String, String>();

        HttpSession session = request.getSession(false);
        Enumeration e = session.getAttributeNames();

        if(e.hasMoreElements()) {
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, (String) session.getAttribute(name));
            }
        } else {
            response.sendError(409, "no values");
	    return;
        }

        String equation = map.get("equation");

        String parameter = "";

        StringBuilder numericEquation = new StringBuilder();
        for (int i = 0; i < equation.length(); i++) {
            if (SomeHelper.isLetter(equation.charAt(i))) {
                if(map.containsKey(Character.toString(equation.charAt(i)))) {
                    parameter = map.get(Character.toString(equation.charAt(i)));
                } else {
                    response.sendError(409, "lack of data");
                    return;
                }
                while (SomeHelper.isLetter(parameter.charAt(0))) {
                    if(map.containsKey(Character.toString(parameter.charAt(0)))) {
                        parameter = map.get(Character.toString(parameter.charAt(0)));
                    }
                    else {
                        response.sendError(409, "lack of data");
                        return;
                    }
                }
                numericEquation.append(parameter);
            } else {
                numericEquation.append(equation.charAt(i));
            }
        }

        System.out.println("!!!!" + numericEquation.toString() + "!!!!");
        out.print(calculation(numericEquation.toString()));
    }
}
