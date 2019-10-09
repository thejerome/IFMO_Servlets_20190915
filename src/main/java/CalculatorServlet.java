import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "CalculatorServlet",
        urlPatterns = {"/calc"}
)
public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String equation = req.getParameter("equation");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < equation.length(); ++i) {
            if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                String val = req.getParameter(String.valueOf(equation.charAt(i)));
                while (val.charAt(0) >= 'a' && val.charAt(0) <= 'z') {
                    val = req.getParameter(String.valueOf(val.charAt(0)));
                }
                sb.append(val);
            } else {
                sb.append(equation.charAt(i));
            }
        }
        equation = sb.toString();
        writer.println(Calculator.decis(equation));
        writer.flush();
        writer.close();
    }
}
