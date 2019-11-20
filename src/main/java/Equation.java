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
        name = "Equation",
        urlPatterns = {"/calc/equation"}
)
public class Equation extends HttpServlet {
void func(PrintWriter out, HttpServletResponse resp) {
    resp.setStatus(400);
    out.print("Badly formatted value");
    out.flush();
    out.close();
}
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = resp.getWriter();
        BufferedReader reader = req.getReader();
        String str = reader.readLine();
        char prev = '\0';
        int cg = 0;
        char[] modequation = str.toCharArray();
        for (char c : modequation) {
            if (Character.isLowerCase(c))
                if (cg == 1) {
                    func(out, resp);
                    return;
                } else
                    cg = 1;
            if (c == '*' || c == '/' || c == '+' || c == '-')
                if (cg == 2) {
                    func(out, resp);
                    return;
                } else
                    cg = 2;
            if (Character.isDigit(c))
                cg = 3;
        }
        if (session.getAttribute("equation") == null) {
            resp.setStatus(201);
        } else {
            resp.setStatus(200);
        }
        session.setAttribute("equation", str);
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute(req.getRequestURI().substring(6));
        resp.setStatus(204);
    }
}