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

        do {
            equation = next.toString();

            next = new StringBuilder();
            for (int i = 0; i < equation.length(); ++i) {
                if (Character.isAlphabetic(equation.codePointAt(i))) {
                    next.append(req.getParameter(Character.toString(equation.charAt(i))));
                } else {
                    next.append(equation.charAt(i));
                }
            }
        } while (!next.toString().equals(equation));

        return next.toString();
    }

    private int highLevelSolve(String equation) {
        int left;

        do {
            left = 0;

            boolean opened = false;
            StringBuilder next = new StringBuilder();
            for (int i = 0; i < equation.length(); ++i) {
                switch (equation.charAt(i)) {
                    case '(':
                        next.append(equation, left, i);
                        opened = true;
                        left = i;
                        break;

                    case ')':
                        if (opened) {
                            next.append(lowLevelSolve(binaryMinusToUnary(equation.substring(left + 1, i))));
                            opened = false;
                            left = i + 1;
                        }
                }

                if (equation.charAt(i) != ')' && i == equation.length() - 1) {
                    next.append(equation, left, i + 1);
                }
            }

            equation = reduceUnary(next.toString());
        } while (left != 0);

        return lowLevelSolve(equation);
    }

    private String reduceUnary(String equation) {
        String next = equation;

        do {
            equation = next;

            next = equation.replaceAll("--|\\+\\+", "+")
                    .replaceAll("\\+-", "-");
        } while (!equation.equals(next));

        return next;
    }

    private String binaryMinusToUnary(String equation) {
        return equation.replaceAll("(\\d)-(\\d)", "$1+-$2");
    }

    private int lowLevelSolve(String equation) {
        equation = simpleSolve(equation, eq -> Math.min(indexOf(eq, '*'), indexOf(eq, '/')),
                (operator, operands) -> {
                    if (operator == '*') {
                        return operands[0] * operands[1];
                    } else if (operator == '/') {
                        return operands[0] / operands[1];
                    } else {
                        throw new RuntimeException();
                    }
                });

        equation = simpleSolve(equation, eq -> indexOf(eq, '+'), (operator, operands) -> operands[0] + operands[1]);
        return Integer.parseInt(equation);
    }

    private String simpleSolve(
            String equation,
            ToIntFunction<String> nextIndex,
            ToIntBiFunction<Character, int[]> operator
    ) {
        for (int i = nextIndex.applyAsInt(equation); i < equation.length(); i = nextIndex.applyAsInt(equation)) {
            int l = i - 1, r = i + 1;

            while (l > -1 && (Character.isDigit(equation.charAt(l)) || equation.charAt(l) == '-')) {
                --l;
            }
            ++l;

            while (r < equation.length() && (Character.isDigit(equation.charAt(r)) || equation.charAt(r) == '-')) {
                ++r;
            }

            int[] operands = Stream.of(Pattern.compile(Character.toString(equation.charAt(i)), Pattern.LITERAL)
                    .split(equation.substring(l, r))).mapToInt(Integer::parseInt).toArray();

            equation = equation.substring(0, l) + operator.applyAsInt(equation.charAt(i), operands) + equation.substring(r);
        }

        return equation;
    }

    private int indexOf(String str, char c) {
        int i = str.indexOf(c);

        if (i == -1) {
            return str.length();
        }

        return i;
    }
}
