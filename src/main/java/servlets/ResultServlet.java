package servlets;

import main.Calculator;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;

@WebServlet(name = "ResultServlet", urlPatterns = "/calc/result")
public class ResultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest requset, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        HttpSession session = requset.getSession();
        Enumeration<String> attributes = session.getAttributeNames();
        if (session.getAttribute("equation") != null) {
            String temp = session.getAttribute("equation").toString();
            while (attributes.hasMoreElements()) {
                String attribute = attributes.nextElement();
                String attributeValue = session.getAttribute(attribute).toString();
                if (attributeValue.length() == 1 && (attributeValue.charAt(0) >= 'a' && attributeValue.charAt(0) <= 'z')) {
                    session.setAttribute(attribute, session.getAttribute(attributeValue));
                }
                temp = temp.replace(attribute, session.getAttribute(attribute).toString());
            }
            Calculator calculator = new Calculator();
            temp = temp.replace(" ", "");
            calculator.setInfix(temp);
            calculator.constructTree();
            int res = calculator.getResult();
            writer.print(res);
        }
        writer.flush();
        writer.close();
    }
}