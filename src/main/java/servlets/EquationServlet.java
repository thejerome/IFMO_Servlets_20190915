package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(name = "EquationServlet", urlPatterns = "/calc/equation")
public class EquationServlet extends HttpServlet {
    protected void doPut(HttpServletRequest requset, HttpServletResponse response) throws IOException {
        String temp;
        BufferedReader reader = requset.getReader();
        HttpSession session = requset.getSession();
        temp = reader.readLine();
        if (session.getAttribute("equation") != null) {
            String temp1 = session.getAttribute("equation").toString();
            if (!temp.equals(temp1)) {
                session.setAttribute("equation", temp);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            session.setAttribute("equation", temp);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    protected void doDelete(HttpServletRequest requset, HttpServletResponse response){
        HttpSession session = requset.getSession();
        if (session != null && (session.getAttribute("equation") != null)) {
            session.removeAttribute("equation");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}