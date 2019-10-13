
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javafx.util.Pair;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = ("/calc")
)

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(calculate(req));
    }

    private String calculate(HttpServletRequest req) {
        Map<String, String[]> p = req.getParameterMap();

        String equation = toNormForm(p);

        while (equation.indexOf('(') != -1) {
            int leftBorder = equation.lastIndexOf('(');
            int rightBorder = equation.indexOf(')', leftBorder);
            if (rightBorder < equation.length() - 1)
                equation = equation.substring(0,leftBorder) +
                        doOperations(equation.substring(leftBorder, rightBorder + 1)) +
                        equation.substring(rightBorder + 1);
            else
                equation = equation.substring(0,leftBorder) +
                        doOperations(equation.substring(leftBorder, rightBorder + 1));
        }
        return equation;
    }


    private String toNormForm(Map<String, String[]> p) {
        HashMap<String, String> variables = new HashMap<>();
        String equation = "";
        for (String k : p.keySet()) {
            if ("equation".equals(k)) {
                equation = p.get(k)[0];
            } else if (variables.containsKey(p.get(k)[0])) {
                variables.put(k, variables.get(p.get(k)[0]));
            } else {
                variables.put(k, p.get(k)[0]);
            }
        }
        equation = equation.replace(" ", "");
        equation = equation.replace("-", "+-");
        StringJoiner sJoiner = new StringJoiner("");
        String[] elements = equation.split("");
        for (int i = 0; i < elements.length; i++) {
            if (variables.containsKey(elements[i])) {
                elements[i] = variables.get(elements[i]);
            }
            sJoiner.add(elements[i]);
        }
        return '(' + sJoiner.toString() + ')';
    }

    private String doOperations(String partOfEquation) {
        String result = partOfEquation;
        int i = 0;
        while (i != -1) {
            i = findNextOperation(result);
            if (i != -1) {
                Pair<Integer, Integer> p = findBorderOfOperation(result, i);
                int res = doThisOperation(result.substring(p.getKey() + 1, p.getValue()), result.charAt(i));
                result = result.substring(0, p.getKey() + 1) + res + result.substring(p.getValue());
            }
        }
        return result.substring(1, result.length() - 1);
    }

    private int findNextOperation(String partOfEquation) {
        int firstMul = partOfEquation.indexOf('*');
        int firstDiv = partOfEquation.indexOf('/');
        int i = -1;
        if (firstMul != -1) {
            i = firstMul;
            if (firstDiv != -1) i = (firstMul < firstDiv) ? firstMul : firstDiv;
        } else {
            if (firstDiv != -1) i = firstDiv;
        }/*
        if (i == -1) {
            int firstPlus = partOfEquation.indexOf('+');
            int firstMinus = partOfEquation.indexOf('-');
            if (firstPlus != -1) {
                i = firstPlus;
                if (firstMinus != -1) i = (firstPlus < firstMinus) ? firstPlus : firstMinus;
            } else {
                if (firstMinus != -1) i = firstMinus;
            }
        }*/
        if (i == -1) i = partOfEquation.indexOf('+');
        return i;
    }

    private Pair<Integer, Integer> findBorderOfOperation(String partOfEquation, int i) {
        int l = i - 1;
        while ( partOfEquation.charAt(l) != '(' &&
                partOfEquation.charAt(l) != '+' &&
                //partOfEquation.charAt(l) != '-' &&
                partOfEquation.charAt(l) != '*' &&
                partOfEquation.charAt(l) != '/') l--;
        int r = i + 1;
        while ( partOfEquation.charAt(r) != ')' &&
                partOfEquation.charAt(r) != '+' &&
                //partOfEquation.charAt(r) != '-' &&
                partOfEquation.charAt(r) != '*' &&
                partOfEquation.charAt(r) != '/') r++;
        return new Pair<>(l, r);
    }

    private int doThisOperation(String partOfEquation, char c) {
        int i = partOfEquation.indexOf(c);
        int leftValue = Integer.parseInt(partOfEquation.substring(0, i));
        int rightValue = Integer.parseInt(partOfEquation.substring(i + 1));
        int result = leftValue;
        if (partOfEquation.charAt(i) == '*') result *= rightValue;
        else if (partOfEquation.charAt(i) == '/') result /= rightValue;
        else if (partOfEquation.charAt(i) == '+') result += rightValue;
        //else if (partOfEquation.charAt(i) == '-') result -= rightValue;
        return result;
    }
}

