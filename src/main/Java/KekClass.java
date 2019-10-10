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

        if (stringBuilder.charAt(0) == '-'){
            equation = "0" + stringBuilder.toString();
        } else {
            equation = stringBuilder.toString();
        }

        ArrayList<Character> operatorStack = new ArrayList<>();
        ArrayList<String> outputStack = new ArrayList<>();
        StringBuilder tempNumber = new StringBuilder();
        for (int i = 0; i < equation.length(); i++){
            if (equation.charAt(i) >= '0' && equation.charAt(i) <= '9'){

                if (tempNumber.length() != 0){

                    tempNumber.append(equation.charAt(i));

                } else {

                    tempNumber.append(equation.charAt(i));
                }
            } else {

                if (tempNumber.length() != 0) {
                    outputStack.add(tempNumber.toString());
                    tempNumber.setLength(0);
                }

                switch (equation.charAt(i)){
                    case ('*'):
                    case ('/'):
                        operatorStack.add(equation.charAt(i));
                        break;
                    case ('+'):
                    case ('-'):
                        if(!operatorStack.isEmpty()) {
                            while (!operatorStack.isEmpty()){
                                if (operatorStack.get(operatorStack.size() - 1) != '(' ) {
                                    outputStack.add(operatorStack.get(operatorStack.size() - 1).toString());
                                    operatorStack.remove(operatorStack.size() - 1);
                                } else {
                                    break;
                                }
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
        while (outputStack.size() > 1) {
            for (int i = 1; i < outputStack.size() - 1; i++){
                if (       !outputStack.get(i-1).equals("+")
                        && !outputStack.get(i-1).equals("-")
                        && !outputStack.get(i-1).equals("*")
                        && !outputStack.get(i-1).equals("/")
                        && !outputStack.get( i ).equals("+")
                        && !outputStack.get( i ).equals("-")
                        && !outputStack.get( i ).equals("*")
                        && !outputStack.get( i ).equals("/")
                        && (outputStack.get(i+1).equals("+")
                        ||  outputStack.get(i+1).equals("-")
                        ||  outputStack.get(i+1).equals("*")
                        ||  outputStack.get(i+1).equals("/"))){
                    int leftNumber = Integer.parseInt(outputStack.get(i-1));
                    int rightNumber = Integer.parseInt(outputStack.get(i));
                    String operator = outputStack.get(i+1);
                    outputStack.remove(i+1);
                    outputStack.remove(i+0);
                    outputStack.remove(i-1);
                    switch (operator){
                        case ("+"):
                            outputStack.add(i-1, Integer.toString(leftNumber + rightNumber));
                            break;
                        case ("-"):
                            outputStack.add(i-1, Integer.toString(leftNumber - rightNumber));
                            break;
                        case ("*"):
                            outputStack.add(i-1, Integer.toString(leftNumber * rightNumber));
                            break;
                        case ("/"):
                            outputStack.add(i-1, Integer.toString(leftNumber / rightNumber));
                            break;
                    }
                }
            }
        }
        outputStream.write(outputStack.get(0));
        outputStream.flush();
        outputStream.close();
    }
}
