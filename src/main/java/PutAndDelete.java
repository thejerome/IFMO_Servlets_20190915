import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "PutAndDelete",
        urlPatterns = {"/calc/equation"}
)

public class PutAndDelete extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession sess = req.getSession();
        PrintWriter out = res.getWriter();
        String str = req.getReader().readLine();
        if (checkbadformat(str)) {
            res.setStatus(400);
            out.println("bad format");
        }
        else {
            if (sess.getAttribute("equation") == null) res.setStatus(201);
            else res.setStatus(200);
            sess.setAttribute("equation", str);
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        HttpSession sess = req.getSession();
        sess.setAttribute("equation", null);
        res.setStatus(204);
    }

    private boolean checkbadformat(String str) {
        int cnt = 0;
        for (int i=0; i<str.length(); i++) {
            if(isOperator(str.charAt(i))) cnt++;
            if(str.charAt(i)>='A' && str.charAt(i)<='Z') return true;
        }
        return cnt == 0;
    }

    private boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/');
    }
}

