import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(
        name = "CalculatorServlet",
        urlPatterns = {"/calc"}
)
public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String equation = request.getParameter("equation");
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder stringBuilder = new StringBuilder();
        for (int symbol = 0; symbol < equation.length(); ++symbol){
            if(equation.charAt(symbol) >= 'a' && equation.charAt(symbol) <= 'z') {
                String param = paramMap.get(Character.toString(equation.charAt(symbol)))[0];
                while (param.charAt(0) >= 'a' && param.charAt(0) <= 'z') {
                    param = paramMap.get(param)[0];
                }
                stringBuilder.append(param);
            }
                else {
                    stringBuilder.append(equation.charAt(symbol));
                }
        }
        equation = stringBuilder.toString();
        ServletOutputStream out = response.getOutputStream();
        out.write(Integer.toString(Calculator.calc(equation)).getBytes());
        out.flush();
        out.close();
    }
}
