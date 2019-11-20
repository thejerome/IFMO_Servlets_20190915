import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

@WebServlet(
        name = "Result",
        urlPatterns = {"/calc/result"}
)
public class Result extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = resp.getWriter();
        String equation = (String) session.getAttribute("equation");
        int a = 0;
        char[] modequation;
        while (a != 2) {
            modequation = equation.toCharArray();
            for (char c : modequation)
                if (Character.isLetter(c)) {
                    equation = equation.replaceAll(String.valueOf(c),
                            String.valueOf(session.getAttribute(String.valueOf(c))));
                }
            a++;
        }
        Calculater calculater = new Calculater();
        int result = calculater.calculate(equation);
        out.print(result);
        out.flush();
        out.close();
    }

}

