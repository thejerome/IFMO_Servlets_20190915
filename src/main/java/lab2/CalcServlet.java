package lab2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc/result"}
)
public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        HttpSession httpSession = req.getSession();
        PrintWriter writer = resp.getWriter();
        if (httpSession == null) {
            resp.setStatus(409);
            writer.write("Values are not available!");
        } else if (httpSession.getAttribute("equation")==null){
            resp.setStatus(409);
            writer.write("Equations are not available!");
            } else {
                    try{
                        String equation = (String) httpSession.getAttribute("equation");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < equation.length(); ++i) {
                            if (equation.charAt(i) >= 'a' && equation.charAt(i) <= 'z') {
                                String val = (String) httpSession.getAttribute(String.valueOf(equation.charAt(i)));
                                while (val.charAt(0) >= 'a' && val.charAt(0) <= 'z') {
                                    val = (String) httpSession.getAttribute(val);
                                }
                                sb.append(val);
                            } else {
                                sb.append(equation.charAt(i));
                            }
                        }
                        equation = sb.toString();
                        writer.print(CalcUtils.deci(equation));
                    } catch (Exception e) {
                        resp.setStatus(409);
                        writer.write("Incorrect inputs!");
                        }
                    }
        writer.flush();
        writer.close();
    }
}
