package lab2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "Variable",
        urlPatterns = {"/calc/*"}
)
public class Variable extends HttpServlet {
    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {

            Double.parseDouble(strNum);

        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String s = req.getRequestURI().substring(6);
        PrintWriter out = resp.getWriter();
        BufferedReader in = req.getReader();
        String line = in.readLine();
        if (!((line.length() == 1 && Character.isLowerCase(line.charAt(0)))
                || isNum(line))) {
            resp.setStatus(400);
            out.flush();
            out.close();
            return;
        } else if (isNum(line) && Math.abs(Integer.parseInt(line)) >= 10000) {
            resp.setStatus(403);
            out.flush();
            out.close();
            return;
        } else if (session.getAttribute(s) == null) {
            resp.setStatus(201);
        } else {
            resp.setStatus(200);
        }
        session.setAttribute(s, line);
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null)
            session.removeAttribute(req.getRequestURI().substring(6));
        resp.setStatus(204);
    }
}