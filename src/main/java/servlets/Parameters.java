package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/calc/*")
public class Parameters extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        final String[] part = req.getRequestURI().split("/");
        final String param = part[part.length - 1];

        req.getSession().removeAttribute(param);
        resp.setStatus(204);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String[] part = req.getRequestURI().split("/");
        final String param = part[part.length - 1];

        if ("equation".equals(param)) {
            parseEquation(req, resp);
            return;
        }

        if (param.length() == 1) {
            parseParameter(param, req, resp);
            return;
        }

        resp.sendError(400, "Unknown parameter");
    }

    private void parseEquation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final boolean hasBeen = req.getSession().getAttribute("equation") != null;
        final String value = req.getReader().readLine().replaceAll("\\s", "");

        int counter = 0;
        for (char c : value.toCharArray()) {
            if (c == '(') {
                ++counter;
            } else if (c == ')') {
                --counter;
            }
        }

        if (counter != 0 || !Pattern.matches("(([a-z]|-?\\d+)[+*/-])*([a-z]|-?\\d+)", value.replaceAll("[()]", ""))) {
            resp.sendError(400, "Invalid equation");
            return;
        }

        req.getSession().setAttribute("equation", value);
        createdAnswer(hasBeen, req, resp);
    }

    private void parseParameter(String parameter, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final boolean hasBeen = req.getSession().getAttribute(parameter) != null;
        final String value = req.getReader().readLine().trim();

        try {
            final long numeric = Long.parseLong(value);

            if (numeric < -10000 || numeric > 10000) {
                resp.sendError(403, "Value isn't includes in the [-10000, 10000] range");
                return;
            }

            req.getSession().setAttribute(parameter, value);
            createdAnswer(hasBeen, req, resp);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (value.length() == 1) {
            req.getSession().setAttribute(parameter, value);
            createdAnswer(hasBeen, req, resp);
            return;
        }

        resp.sendError(400, "Invalid parameter value");
    }

    private void createdAnswer(boolean hasBeen, HttpServletRequest req, HttpServletResponse resp) {
        if (hasBeen) {
            resp.setStatus(200);
        } else {
            resp.setHeader("Location", String.valueOf(req.getRequestURL()));
            resp.setStatus(201);
        }
    }
}