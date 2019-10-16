import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(
        name = "Vars",
        urlPatterns = {"/calc/*"}
)
public class Vars extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sesh = req.getSession();
        // code below taken from vitaly don't ban please
        // get variables from request body
        String varName = req.getRequestURI().substring(6); // why 6? len("/calc/") = 6? so it does varName = requetURI[6:]
        String varVal = req.getReader().lines().collect(Collectors.joining(System.lineSeparator())); // wicked
        if (sesh.getAttribute("vars") == null) {
            sesh.setAttribute("vars", new HashMap<String, Object>());
        }
        Map<String, Object> varsMap = (Map<String, Object>) sesh.getAttribute("vars");

        if (varsMap.containsKey(varName)) {
            resp.setStatus(200);
        }
        else {
            resp.setStatus(201);
        }
        // update hashmap
        varsMap.put(
                varName,
                varVal // unlike vitaly my value must always be string
        );
        sesh.setAttribute("vars", varsMap);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession sesh = req.getSession();
        Map<String, Object> varsMap = (HashMap<String, Object>) sesh.getAttribute("vars");
        String varName = req.getRequestURI().substring(6);
        varsMap.remove(varName);
        resp.setStatus(204);
    }
}
