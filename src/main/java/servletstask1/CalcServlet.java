package servletstask1;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(highLevelSolve(
                substituteVariables(req.getParameter("equation").replaceAll("\\s+", ""), req)
        ));
    }

    private String substituteVariables(String equation, HttpServletRequest req) {
        StringBuilder next = new StringBuilder(equation);
        String eq;

        do {
            eq = next.toString();

            next = new StringBuilder();
            for (int i = 0; i < eq.length(); ++i) {
                if (Character.isAlphabetic(eq.codePointAt(i))) {
                    next.append(req.getParameter(Character.toString(eq.charAt(i))));
                } else {
                    next.append(eq.charAt(i));
                }
            }
        } while (!next.toString().equals(eq));

        return next.toString();
    }

    private int highLevelSolve(String equation) {
        String eq = equation;
        int left;

        do {
            left = 0;

            boolean opened = false;
            StringBuilder next = new StringBuilder();
            for (int i = 0; i < eq.length(); ++i) {
                if (eq.charAt(i) == '(') {
                    next.append(eq, left, i);
                    opened = true;
                    left = i;
                } else if (eq.charAt(i) == ')' && opened) {
                    next.append(lowLevelSolve(binaryMinusToUnary(eq.substring(left + 1, i))));
                    opened = false;
                    left = i + 1;
                }

                if (eq.charAt(i) != ')' && i == eq.length() - 1) {
                    next.append(eq, left, i + 1);
                }
            }

            eq = reduceUnary(next.toString());
        } while (left != 0);

        return lowLevelSolve(eq);
    }

    private String reduceUnary(String equation) {
        String eq = equation;
        String next = eq;

        do {
            eq = next;

            next = eq.replaceAll("--|\\+\\+", "+")
                    .replaceAll("\\+-", "-");
        } while (!eq.equals(next));

        return next;
    }

    private String binaryMinusToUnary(String equation) {
        return equation.replaceAll("(\\d)-(\\d)", "$1+-$2");
    }

    private int lowLevelSolve(String equation) {
        String eq = equation;

        eq = simpleSolve(eq, eq1 -> Math.min(indexOf(eq1, '*'), indexOf(eq1, '/')),
                (operator, operands) -> {
                    if (operator == '*') {
                        return operands[0] * operands[1];
                    } else {
                        return operands[0] / operands[1];
                    }
                });

        eq = simpleSolve(eq, eq1 -> indexOf(eq1, '+'), (operator, operands) -> operands[0] + operands[1]);
        return Integer.parseInt(eq);
    }

    private String simpleSolve(
            String equation,
            ToIntFunction<String> nextIndex,
            ToIntBiFunction<Character, int[]> operator
    ) {
        String eq = equation;

        for (int i = nextIndex.applyAsInt(eq); i < eq.length(); i = nextIndex.applyAsInt(eq)) {
            int l = i - 1;
            int r = i + 1;

            while (l > -1 && (Character.isDigit(eq.charAt(l)) || eq.charAt(l) == '-')) {
                --l;
            }
            ++l;

            while (r < eq.length() && (Character.isDigit(eq.charAt(r)) || eq.charAt(r) == '-')) {
                ++r;
            }

            int[] operands = Stream.of(Pattern.compile(Character.toString(eq.charAt(i)), Pattern.LITERAL)
                    .split(eq.substring(l, r))).mapToInt(Integer::parseInt).toArray();

            eq = eq.substring(0, l) + operator.applyAsInt(eq.charAt(i), operands) + eq.substring(r);
        }

        return eq;
    }

    private int indexOf(String str, char c) {
        int i = str.indexOf(c);

        if (i == -1) {
            return str.length();
        }

        return i;
    }
}
