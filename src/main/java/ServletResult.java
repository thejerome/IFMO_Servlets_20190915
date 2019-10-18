import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(
        name = "resultServlet",
        urlPatterns = {"/calc/result"}
)

public class ServletResult extends HttpServlet {

    public String calculateRPN(ArrayList<String> equation) {
        while (equation.size() > 1) {
            for (int i = 1; i < equation.size() - 1; i++) {
                if (   !equation.get(i - 1).equals("+")
                        && !equation.get(i - 1).equals("-")
                        && !equation.get(i - 1).equals("*")
                        && !equation.get(i - 1).equals("/")
                        && !equation.get(i    ).equals("+")
                        && !equation.get(i    ).equals("-")
                        && !equation.get(i    ).equals("*")
                        && !equation.get(i    ).equals("/")
                        && (equation.get(i + 1).equals("+")
                        ||  equation.get(i + 1).equals("-")
                        ||  equation.get(i + 1).equals("*")
                        ||  equation.get(i + 1).equals("/"))) {

                    int leftNumber = Integer.parseInt(equation.get(i - 1));
                    int rightNumber = Integer.parseInt(equation.get(i));
                    String operator = equation.get(i + 1);
                    for (int j = i + 1; j >= i - 1; j--) {
                        equation.remove(j);
                    }
                    switch (operator) {
                        case ("+"):
                            equation.add(i - 1, Integer.toString(leftNumber + rightNumber));
                            break;
                        case ("-"):
                            equation.add(i - 1, Integer.toString(leftNumber - rightNumber));
                            break;
                        case ("*"):
                            equation.add(i - 1, Integer.toString(leftNumber * rightNumber));
                            break;
                        case ("/"):
                            equation.add(i - 1, Integer.toString(leftNumber / rightNumber));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return equation.get(0);
    }

    public ArrayList<String> buildRPN(String equation){
        ArrayList<Character> operatorStack = new ArrayList<>();
        ArrayList<String> outputStack = new ArrayList<>();
        StringBuilder tempNumber = new StringBuilder();
        for (int i = 0; i < equation.length(); i++){
            if (Character.toString(equation.charAt(i)).matches("[0-9]")){
                tempNumber.append(equation.charAt(i));
            } else {
                if (tempNumber.length() != 0) {
                    outputStack.add(tempNumber.toString());
                    tempNumber.setLength(0);
                }
                switch (equation.charAt(i)){
                    case ('*'):
                    case ('/'):
                        if (!operatorStack.isEmpty() && Character.toString(operatorStack.get(operatorStack.size() - 1)).matches("[/*]")){
                            outputStack.add(operatorStack.get(operatorStack.size() - 1).toString());
                            operatorStack.remove(operatorStack.size() - 1);
                        }
                        operatorStack.add(equation.charAt(i));
                        break;
                    case ('+'):
                    case ('-'):
                        while (!operatorStack.isEmpty()){
                            if (operatorStack.get(operatorStack.size() - 1) != '(' ) {
                                outputStack.add(operatorStack.get(operatorStack.size() - 1).toString());
                                operatorStack.remove(operatorStack.size() - 1);
                            } else {
                                break;
                            }
                        }
                        operatorStack.add(equation.charAt(i));
                        break;
                    case ('('):
                        operatorStack.add('(');
                        break;
                    case (')'):
                        while (operatorStack.get(operatorStack.size() - 1) != '('){
                            outputStack.add(operatorStack.get(operatorStack.size() - 1).toString());
                            operatorStack.remove(operatorStack.size() - 1);
                        }
                        operatorStack.remove(operatorStack.size() - 1);
                    default:
                        break;
                }
            }
        }
        if (tempNumber.length() != 0) {
            outputStack.add(tempNumber.toString());
            tempNumber.setLength(0);
        }
        while (!operatorStack.isEmpty()){
            outputStack.add(operatorStack.get(operatorStack.size() - 1).toString());
            operatorStack.remove(operatorStack.size() - 1);
        }
        System.out.println(outputStack);
        return outputStack;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter output = resp.getWriter();
        HttpSession session = req.getSession();
        if (session == null){
            resp.setStatus(409);
            output.write("Values are not available!");
        } else if (session.getAttribute("equation") == null){
            resp.setStatus(409);
        } else {
            String equation = session.getAttribute("equation").toString();
            StringBuilder stringBuilder = new StringBuilder();
            for (char c: equation.toCharArray()) {
                if (Character.toString(c).matches("[a-z]")){
                    if (session.getAttribute(Character.toString(c)) != null) {
                        String equationCharacter = session.getAttribute(Character.toString(c)).toString();
                        while (Character.toString(equationCharacter.charAt(0)).matches("[a-z]")) {
                            equationCharacter = session.getAttribute(equationCharacter).toString();
                            System.out.println("kek");
                        }
                        if (Integer.parseInt(equationCharacter) < 0) {
                            stringBuilder.append("(0" + equationCharacter + ")");
                        } else {
                            stringBuilder.append(equationCharacter);
                        }
                    } else {
                        resp.setStatus(409);
                        return;
                    }
                } else {
                    stringBuilder.append(c);
                }
            }
            output.write(calculateRPN(buildRPN(stringBuilder.toString())));
        }
        output.flush();
        output.close();
    }
}
