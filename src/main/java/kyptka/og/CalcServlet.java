package kyptka.og;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@WebServlet(
        name = "CalcServlet",
        urlPatterns = {"/calc/result"}
)
public class CalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        Map<String, String> map = new HashMap<>();
        Enumeration<String> enumeration = session.getAttributeNames();

        if(enumeration.hasMoreElements()) {
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                map.put(name, (String) session.getAttribute(name));
            }
        } else {
            resp.sendError(409, "error 409");
            return;
        }

        String equation = map.get("equation");
        if (equation != null) {
            try {
                resp.setStatus(200);
                resp.getWriter().print(new Calculator(equation, map).solve());
            } catch (Exception exception) {
                resp.sendError(409, "error 409");
            }
        } else {
            resp.sendError(409, "error 409");
        }
//        if (equation != null) {
    }
}
