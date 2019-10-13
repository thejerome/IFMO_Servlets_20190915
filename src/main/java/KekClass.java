import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


@WebServlet(
        name = "KekServlet",
        urlPatterns = {"/calc"}
)
public class KekClass extends HttpServlet {

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
            if (equation.charAt(i) >= '0' && equation.charAt(i) <= '9'){
                tempNumber.append(equation.charAt(i));
            } else {
                if (tempNumber.length() != 0) {
                    outputStack.add(tempNumber.toString());
                    tempNumber.setLength(0);
                }
                switch (equation.charAt(i)){
                    case ('*'):
                    case ('/'):
                        if (!operatorStack.isEmpty() && (operatorStack.get(operatorStack.size() - 1) == '*' || operatorStack.get(operatorStack.size() - 1) == '/')) {
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
        return outputStack;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter outputStream = new PrintWriter(resp.getOutputStream());
        String equation = req.getParameter("equation");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < equation.length(); i++) {
            String equationCharacter = Character.toString(equation.charAt(i));
            while (equationCharacter.charAt(0) >= 'a' && equationCharacter.charAt(0) <= 'z'
                || equationCharacter.charAt(0) >= 'A' && equationCharacter.charAt(0) <= 'Z') {
                equationCharacter = req.getParameter(Character.toString(equationCharacter.charAt(0)));
            }
            stringBuilder.append(equationCharacter);
        }

        outputStream.write(calculateRPN(buildRPN(stringBuilder.toString())));
        outputStream.flush();
        outputStream.close();
    }
}
