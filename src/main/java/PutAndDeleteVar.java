import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "PutAndDeleteVar",
        urlPatterns = {"/calc/*"}
)

public class PutAndDeleteVar extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession sess = req.getSession();
        String uri = req.getRequestURI();
        String val = String.valueOf(uri.charAt(uri.length()-1));
        PrintWriter out = res.getWriter();
        String str = req.getReader().readLine();
        if (checkbadformat(str)) {
            res.setStatus(403);
            out.println("bad format");
        }
        else {
            if (sess.getAttribute(val) == null) res.setStatus(201);
            else res.setStatus(200);
            sess.setAttribute(val, str);
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        HttpSession sess = req.getSession();
        sess.setAttribute(String.valueOf(req.getRequestURI().charAt(req.getRequestURI().length()-1)), null);
        res.setStatus(204);
    }

    private boolean checkbadformat(String str) {
        if (str.charAt(0)>='a' && str.charAt(0)<='z') return false;
        return (Integer.parseInt(str) < -10000 || Integer.parseInt(str) > 10000);
    }
}

