package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        urlPatterns = {"/calc"}
)
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getParameter("equation");
        PrintWriter out = resp.getWriter();
        out.print(getResult(equation, req));
    }

    private int getResult(String equation, HttpServletRequest request) {
        StringBuilder numericEquation = new StringBuilder();
        for (int i = 0; i < equation.length(); i++) {
            if (Helper.isLetter(equation.charAt(i))) {
                String parameter = request.getParameter(Character.toString(equation.charAt(i)));
                while (Helper.isLetter(parameter.charAt(0))) {
                    parameter = request.getParameter(Character.toString(parameter.charAt(0)));
                }
                numericEquation.append(parameter);
//            } else if (isAnyOperator(equation.charAt(0))) {
//                numericEquation.append(equation.charAt(i));
            } else {
                numericEquation.append(equation.charAt(i));
            }
        }
        return Helper.calculation(numericEquation.toString());
    }
}

