package calcservlet;

import calculator.Calculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "ResultServlet", urlPatterns = "/calc/result")
public class ResultServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        if (session.getAttribute("equation")!=null) {
            String qtn = session.getAttribute("equation").toString();
            while (attributeNames.hasMoreElements()) {
                String attribute = attributeNames.nextElement();
                String attrVal = session.getAttribute(attribute).toString();
                if (1 == attrVal.length() && (attrVal.charAt(0) >= 'a' && attrVal.charAt(0) <= 'z')) {
                    session.setAttribute(attribute, session.getAttribute(attrVal));
                }
                qtn = qtn.replace(attribute, session.getAttribute(attribute).toString());
            }
            Calculator calc = new Calculator();
            qtn = qtn.replace(" ", "");
            calc.setInfix(qtn);
            calc.constructTree();
            int res = calc.getResult();
            out.print(res);
        }
        out.flush();
        out.close();
    }
}
