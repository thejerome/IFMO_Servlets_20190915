package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;

@WebServlet(
        name = "Servlet1",
        urlPatterns = {"/calc"}
)

public class Servlet1 extends HttpServlet {

    private Map<Character, Integer> priorities = new HashMap<>();

    public Servlet1() {
        priorities.put('+', 1);
        priorities.put('-', 1);
        priorities.put('*', 2);
        priorities.put('/', 2);
        priorities.put('(', 0);
        priorities.put(')', 0);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String equation = req.getParameter("equation");
        if (equation == null) {
            showFailResponse(resp);
            return;
        }

        Map<Character, Integer> values = new TreeMap<>();

        Map<String, String[]> passedValues = req.getParameterMap();
        passedValues.forEach(new BiConsumer<String, String[]>() {
            @Override
            public void accept(String s, String[] strings) {
                if (s.isEmpty() || !isParameterName(s.charAt(0)) || strings.length == 0 || !isNumber(strings[0]) || s.equals("equation")) {
                    if (s.equals("equation")) return;
                    try {
                        showFailResponse(resp);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    values.put(s.charAt(0), Integer.valueOf(strings[0]));
                }
            }
        });

        int result = calculate(equation, values);

        showResult(result, resp);
    }

    private int calculate(String equation, Map<Character, Integer> values) {
        Deque<Integer> numbers = new ArrayDeque<>();
        Deque<Character> operations = new ArrayDeque<>();
        for (int i = 0; i < equation.length() ; i++) {
            char current = equation.charAt(i);
            if (isParameterName(current)) {
                numbers.addLast(values.get(current));
            } else {
                updateStacks(numbers, operations, current);
            }
        }

        while (numbers.size() > 1) {
            doCurrentStackOperation(numbers, operations);
        }

        return numbers.getFirst();
    }

    private void updateStacks(Deque<Integer> numbers, Deque<Character> operations, char current) {
        while (operations.size() > 0 && operations.getLast() != null && priorities.get(operations.getLast()) >= priorities.get(current) && current != '(') {
            Character lastOperation = operations.getLast();
            if (lastOperation == '(' && current == ')') {
                operations.removeLast();
                return;
            }
            try {
                doCurrentStackOperation(numbers, operations);
            } catch (NullPointerException ex) {
                System.err.println(numbers.toString() + '\n' + operations.toString());
            }
        }
        if (current != ')') operations.addLast(current);
    }

    private void doCurrentStackOperation(Deque<Integer> numbers, Deque<Character> operations) {
        if (numbers.size() > 1 && operations.size() > 0) {
            int second = numbers.pollLast();
            int first = numbers.pollLast();
            numbers.addLast(doSimpleOperation(first, second, operations.pollLast()));
        }
    }

    private boolean isNumber(CharSequence s) {
        try {
            Integer.valueOf(s.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    private boolean isParameterName(char c) {
        return c >= 'a' && c <= 'z';
    }

    private int doSimpleOperation(int arg1, int arg2, char operation) {
        int res = 0;
        switch (operation) {
            case '+': res = arg1 + arg2;
                break;
            case '-': res = arg1 - arg2;
                break;
            case '*': res = arg1 * arg2;
                break;
            case '/': res = arg1/arg2;
                break;
        }
        return res;
    }

    private void showMessage(String msg, HttpServletResponse resp) throws IOException {
        BufferedOutputStream output = new BufferedOutputStream(resp.getOutputStream());
        output.write(msg.getBytes());
        output.flush();
        output.close();
    }

    private void showFailResponse(HttpServletResponse resp) throws IOException {
        showMessage("An error was occurred. Enter correct arguments.", resp);
    }

    private void showResult(int result, HttpServletResponse resp) throws IOException {
        showMessage(String.valueOf(result), resp);
    }

}
