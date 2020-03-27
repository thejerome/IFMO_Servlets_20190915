package task2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static task2.Calculator.getResult;

@WebServlet(
        name="ResServlet",
        urlPatterns = {"/calc/result"}
)
public class ResServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        if (httpSession.getAttribute("equation") == null && httpSession.getAttribute("var") == null && letterz((String) httpSession.getAttribute("equation"))) {
            resp.setStatus(409);
            resp.getOutputStream().write("error".getBytes());
        } else {
            Map<String, Object> varsMap = map(httpSession);
            try {
                int result = getResult((String) httpSession.getAttribute("equation"), varsMap);
//                int result = 0;
                resp.getWriter().write(Integer.toString(result));
            } catch (InvalidParameterException e) {
                resp.setStatus(409);
                resp.getOutputStream().write("var2".getBytes());
            }
            catch (IllegalArgumentException e) {
                resp.setStatus(409);
                resp.getOutputStream().write("wrong".getBytes());
            }
        }
    }

    private boolean letterz(String str) {
        for (char c : str.toCharArray()) {
            if (c >= 'A' && c <= 'Z')
                return true;
        }
        return false;
    }

    private Map<String, Object> map(HttpSession ses) {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Map<String, Object> objectMap = new HashMap<>();
        for (char chr: chars) {
            String str = (String) ses.getAttribute(String.valueOf(chr));
            if (str != null){
                if (Character.isDigit(str.toCharArray()[0]))
                    objectMap.put(String.valueOf(chr), Integer.parseInt(str));
                else {
                    objectMap.put(String.valueOf(chr), str);
                }
            }
        }
        System.out.println(objectMap);
        return objectMap;
    }


}
