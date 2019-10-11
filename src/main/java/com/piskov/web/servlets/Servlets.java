        package com.piskov.web.servlets;

        import javax.servlet.annotation.WebServlet;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.util.Map;
        import java.util.Stack;

@WebServlet(
        name = "Servlets",
        urlPatterns = {"/calc"}
)
public class Servlets extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws  IOException {

        PrintWriter out = resp.getWriter();
        String equation = map(req.getParameterMap(),req.getParameter("equation"));
        out.print (calculate(equation));
        out.flush();
        out.close();
    }
    private static String map(Map<String, String[]> variables, String equation){
        String expression = equation;
        while (consistOfLetters(expression)){
            for (char symbol: expression.toCharArray()) {
                if (symbol >= 'a' && symbol <= 'z') {
                    expression = expression.replace(String.valueOf(symbol), String.valueOf(variables.get(String.valueOf(symbol))[0]));
                }
            }
        }
        return expression;
    }
    private static boolean consistOfLetters(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if ('a' <= expression.charAt(i) && 'z' >= expression.charAt(i)) return true;
        }
        return false;
    }
    private static String expressionParser(String Expr){
        StringBuilder current= new StringBuilder();
        Stack<Character> stack = new Stack<>();

        int priority;
        for (char expression_character: Expr.toCharArray()){
            priority = getPriority(expression_character);

            if (priority == 0) current.append(expression_character);
            else if (priority == 1) stack.push(expression_character);
            else if (priority > 1){
                current.append(' ');
                while(!stack.empty()){
                    if (getPriority(stack.peek()) >= priority ) current.append(stack.pop());
                    else break;
                }
                stack.push(expression_character);
            }
            else if (priority == -1){
                current.append(' ');
                while(getPriority(stack.peek()) != 1) current.append(stack.pop());
                stack.pop();
            }
        }
        while(!stack.empty()) current.append(stack.pop());
        return current.toString();
    }
    private static Integer answer(String rpn){
        StringBuilder operand = new StringBuilder();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < rpn.length(); i++) {
            if (rpn.charAt(i) == ' ') continue;
            if (getPriority(rpn.charAt(i)) == 0) {
                while (rpn.charAt(i) != ' ' && getPriority(rpn.charAt(i)) == 0) {
                    operand.append(rpn.charAt(i++));
                    if (i == rpn.length()) break;
                }
                stack.push(Integer.parseInt(operand.toString()));
                operand = new StringBuilder();
            }
            if (getPriority(rpn.charAt(i)) > 1) {
                Integer a = stack.pop();
                Integer b = stack.pop();

                if (rpn.charAt(i) == '+') stack.push(b + a);
                else if (rpn.charAt(i) == '-') stack.push(b - a);
                else if (rpn.charAt(i) == '*') stack.push(b * a);
                else if (rpn.charAt(i) == '/') stack.push(b / a);
            }
        }
        return stack.pop();

    }

    private static int getPriority(char token){
        if (token == '*' || token == '/') return 3;
        else if (token == '+' || token == '-') return 2;
        else if (token == '(') return 1;
        else if (token == ')') return -1;
        else return 0;
    }

    private static String calculate(String Expression){
        return String.valueOf(answer(expressionParser(Expression)));
    }
}