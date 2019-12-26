package sergey;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(
        urlPatterns = {"/calc/*"}
)
public class EquationServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        String key = req.getPathInfo().replaceAll("/", "");
        String val = req.getReader().readLine();
        try {
            if (key.equals("equation")) {
                parseIntoPolishNotation(val);
            } else if (!Character.isLetter(val.toCharArray()[0])) {
                int arg = Integer.parseInt(val);
                if (arg < -10000 || arg > 10000)
                    throw new DataFormatException();
            }
            if (session.getAttribute(key) == null) {
                resp.setStatus(SC_CREATED);
                resp.setHeader("Location", req.getContextPath());
            } else {
                resp.setStatus(SC_OK);
            }
            session.setAttribute(key, val);
//            System.out.println("New PUT request: " + key + " = " + val);
        } catch (ParseException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (NumberFormatException ee) {
            resp.sendError(SC_BAD_REQUEST, "Wrong number format");
        } catch (DataFormatException eee) {
            resp.sendError(SC_FORBIDDEN, "Exceeding values");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String key = req.getPathInfo().replaceAll("/", "");
        session.setAttribute(key, null);
        resp.setStatus(SC_NO_CONTENT);

//        System.out.print("New delete: " + key);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            HttpSession session = req.getSession();
//            System.out.println("DoGet " + req.getPathInfo() + " with:");
            List<String> tokens = parseIntoPolishNotation(session.getAttribute("equation").toString());
            Map<String, Integer> args = new HashMap<>();
            Map<String, String> StringValue = new HashMap<>();
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String key = names.nextElement();
                if (key.length() != 1) continue;
                String value = session.getAttribute(key).toString();
                try {
                    args.put(key, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    StringValue.put(key, value);
                }
            }
            for (String key : StringValue.keySet()) {
                Integer value = args.get(StringValue.get(key));
                args.put(key, value);
            }
            int result = calculate(tokens, args);

//            System.out.println("args: " + args.toString());
//            System.out.println("result: " + result);

            resp.setStatus(SC_OK);
            resp.getOutputStream().print(result);
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
        } catch (ParseException e) {
            resp.sendError(SC_CONFLICT, "Problem with equation");
        } catch (NumberFormatException ee) {
            resp.sendError(SC_CONFLICT, "Problem with variable");
        } catch (NullPointerException eee) {
            resp.sendError(SC_CONFLICT, "Not set required parameters");
        }
    }

    private List<String> parseIntoPolishNotation(String equation) throws ParseException {
        if (!(equation.contains("+")
                || equation.contains("-")
                || equation.contains("*")
                || equation.contains("/"))) {
            throw new ParseException("notValidEquation", 0);
        }
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(equation.replaceAll(" ", ""), "()+-/*", true);
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }
        Deque<String> stack = new ArrayDeque<>();
        List<String> polishNotation = new ArrayList<>();
        for (String token : tokens) {
            try {
                switch (token) {
                    case "+":
                    case "-":
                    case ")":
                        while ("+-/*".contains(stack.getFirst())) {
                            polishNotation.add(stack.removeFirst());
                        }
                        if (token.equals(")")) {
                            if (!stack.getFirst().equals("("))
                                throw new ParseException("notValidEquation", 0);
                            stack.removeFirst();
                        } else {
                            stack.addFirst(token);
                        }
                        break;
                    case "*":
                    case "/":
                        while ("/*".contains(stack.getFirst())) {
                            polishNotation.add(stack.removeFirst());
                        }
                        stack.addFirst(token);
                        break;
                    case "(":
                        stack.addFirst(token);
                        break;
                    default:
                        polishNotation.add(token);
                }
            } catch (NoSuchElementException e) {
                if (!token.equals(")"))
                    stack.addFirst(token);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.getFirst().equals("("))
                throw new ParseException("notValidEquation", 0);
            polishNotation.add(stack.removeFirst());
        }
        return polishNotation;
    }

    private static int calculate(List<String> polishNotation, Map<String, Integer> args) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : polishNotation) {
            int first;
            int second;
            switch (token) {
                case "+":
                    stack.addFirst(stack.removeFirst() + stack.removeFirst());
                    break;
                case "-":
                    first = stack.removeFirst();
                    second = stack.removeFirst();
                    stack.addFirst(second - first);
                    break;
                case "*":
                    stack.addFirst(stack.removeFirst() * stack.removeFirst());
                    break;
                case "/":
                    first = stack.removeFirst();
                    second = stack.removeFirst();
                    stack.addFirst(second / first);
                    break;
                default:
                    if (args.containsKey(token)) {
                        stack.addFirst(args.get(token));
                    } else {
                        int arg = Integer.parseInt(token);
                        stack.addFirst(arg);
                    }
            }
        }
        return stack.removeFirst();
    }
}
