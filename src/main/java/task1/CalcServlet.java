package task1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet {

    private final Pattern variableRegex = Pattern.compile("[a-z]");
    private final Pattern parenthesesRegex = Pattern.compile("\\([^()]*\\)");
    private final Pattern[] subeqRegex = new Pattern[] {Pattern.compile("-?\\d+[*/]-?\\d+"),
            Pattern.compile("-?\\d+[+-]-?\\d+")};
    private final Pattern opRegex = Pattern.compile("\\d[+*/-][\\d-]");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eq = req.getParameter("equation").replaceAll("\\s", "");

        for (Matcher matcher = variableRegex.matcher(eq); matcher.find(); matcher = variableRegex.matcher(eq)) {
            final int start = matcher.start();
            final int end = matcher.end();

            eq = eq.substring(0, start) + req.getParameter(eq.substring(start, end)) +
                    (end == eq.length() - 1 ? "" : eq.substring(end));
        }

        eq = eq.replaceAll("---", "-");
        eq = eq.replaceAll("--", "+");

        for (Matcher matcher = parenthesesRegex.matcher(eq); matcher.find(); matcher = parenthesesRegex.matcher(eq)) {
            final int start = matcher.start();
            final int end = matcher.end();

            eq = eq.substring(0, start) + calc(eq.substring(start + 1, end - 1)) +
                    (end == eq.length() - 1 ? "" : eq.substring(end));
        }

        resp.getWriter().println(calc(eq));
    }

    private String calc(String equation) {
        String eq = equation;

        for (Pattern subeqRegex : subeqRegex) {
            for (Matcher matcher = subeqRegex.matcher(eq); matcher.find(); matcher = subeqRegex.matcher(eq)) {
                final int start = matcher.start();
                final int end = matcher.end();

                eq = eq.substring(0, start) + calcSubeq(eq.substring(start, end)) +
                        (end == eq.length() - 1 ? "" : eq.substring(end));
            }
        }

        return eq;
    }

    private Long calcSubeq(String subeq) {
        final Matcher matcher = opRegex.matcher(subeq);

        if (!matcher.find()) {
            return null;
        }

        final int pos = matcher.start() + 1;
        final long first = Long.parseLong(subeq.substring(0, pos));
        final long second = Long.parseLong(subeq.substring(pos + 1));

        switch (subeq.charAt(pos)) {
            case '+': return first + second;
            case '-': return first - second;
            case '*': return first * second;
            case '/': return first / second;
            default: return null;
        }
    }
}
