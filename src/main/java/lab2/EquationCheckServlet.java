package lab2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "EquationCheckServlet",
        urlPatterns = {"/calc/equation"}
)
public class EquationCheckServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException{
        HttpSession httpSession = req.getSession();
        PrintWriter writer = resp.getWriter();
        String equation = req.getReader().readLine();
        if (itokay(equation)) {
            if (httpSession.getAttribute("equation") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            httpSession.setAttribute("equation", equation);
        } else {
            resp.setStatus(400);
            writer.write("Badly formatted!");
        }
        writer.flush();
        writer.close();
    }

    private boolean itokay (String equ) {
        int flag = 0;
        for (int i = 0; i < equ.length(); ++i) {
            if (equ.charAt(i)>='A' && equ.charAt(i)<='Z'){
                return false;
            } else if (equ.charAt(i)=='+' || equ.charAt(i)=='-' || equ.charAt(i)=='/' || equ.charAt(i)=='*') {
                flag++;
            }
        }
        return flag != 0;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  {
        resp.setStatus(204);
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("equation", null);
    }
}
