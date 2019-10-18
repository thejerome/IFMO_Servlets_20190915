package calculation;

import instruments.CheckForProblems;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet( urlPatterns = {"/calc/*"})

public class Variables extends HttpServlet{
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        String val = request.getReader().readLine();
        String var = request.getRequestURI().substring(6);

        if (!CheckForProblems.variableCheck(val)) {
            response.setStatus(403);
            response.getWriter().println("wrong data");
        }
        else {
            if (session.getAttribute(var) == null) {
            response.setStatus(201);
        }
            else {
            response.setStatus(200);
        }
        session.setAttribute(var, val);
        }
        //else response.setStatus(200);

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String variable = request.getRequestURI().substring(6);
        session.setAttribute(variable, null);
        response.setStatus(204);
    }
}
