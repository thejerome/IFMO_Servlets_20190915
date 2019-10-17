package com.pilacis.web.servlets;
import java.util.*;

public class Parser {

    private Stack<String> stack = new Stack<String>();
    private ArrayList<String> list = new ArrayList<String>();
    private HashMap<Character, Integer> operators = new HashMap<Character, Integer>();
    private static String expression;
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public Parser(String name, HashMap<String, String> variables){
        String equation = name;
        equation = equation.replace(" ", "");
        for (int i = 0; i < 10; i++) {
            for (String key : variables.keySet()) {
                if (variables.get(key).charAt(0) == '-'){
                    variables.put(key, variables.get(key));
                }
                equation = equation.replace(key, variables.get(key));
            }
        }
        System.out.println(equation);
        this.expression = equation;
        this.operators.put('+', 0); this.operators.put('-', 0);
        this.operators.put('*', 1); this.operators.put('/', 1);
        this.operators.put('(', 4); this.operators.put(')', 4);

    }
    ///////////////////////////////////////////////////////////

    public String getToken(){
        int i = 0;
        boolean isFind = false;
        StringBuilder result = new StringBuilder();

        if (operators.containsKey(this.expression.charAt(0))) {
            isFind = true;
            result.append(this.expression.charAt(i));
            i++;
        }

        if (!isFind) {
            do {
                result.append(Character.toString(this.expression.charAt(i)));
                i++;
            } while ((i < this.expression.length()) && (!operators.containsKey(Character.valueOf(this.expression.charAt(i)))));
        }
        this.expression = expression.substring(i, this.expression.length());
        return result.toString();
    }

    public void parse(){
        while(this.expression.compareTo("") != 0){

            String token = this.getToken();
            // случай, если token - оператор
            if (this.operators.containsKey(token.charAt(0))){


                if (token.charAt(0) == ')'){
                    while ((!this.stack.empty()) && (this.stack.peek().compareTo("(") != 0)){
                        this.list.add(this.stack.pop());
                    }
                    this.stack.pop();
                }
                else{

                    if (token.charAt(0) == '-'){
                        if ((this.list.isEmpty())){
                            this.list.add("0");
                        }
                    }

                    while ((!stack.empty()) &&
                            (this.operators.get(new Character(token.charAt(0))).compareTo(this.operators.get(new Character(this.stack.peek().charAt(0)))) <= 0)
                            && (this.stack.peek().charAt(0) != '(' )) {
                        this.list.add(this.stack.pop());

                    }

                    this.stack.push(token);
                }

            }
            // случай, если token - число
            else{
                this.list.add(token);
            }
        }
        // закидываем оставшуюся часть из стека в список
        while (!this.stack.empty()){
            list.add(stack.pop());
        }


    }

    public int evaluate(){
        Stack<Integer> buffer = new Stack<Integer>();
        for (String str: this.list) {
            if (this.operators.containsKey(str.charAt(0))) {
                int rightOperand = buffer.pop();
                int leftOperand = buffer.pop();
                switch(str.charAt(0)){
                    case('+'):
                        buffer.push(rightOperand+leftOperand);
                        break;
                    case('-'):
                        buffer.push(leftOperand-rightOperand);
                        break;
                    case('*'):
                        buffer.push(leftOperand*rightOperand);
                        break;
                    case('/'):
                        buffer.push(leftOperand/rightOperand);
                        break;
                    default:
                        break;
                }
            }
            else{
                buffer.push(Integer.valueOf(str));
            }
        }
        return buffer.peek();
    }




    public String getStackInfo(){
        ArrayList<String> buffer = new ArrayList<String>();
        String result = "";
        while (!this.stack.empty()){
            buffer.add(this.stack.pop());
        }
        for (int i = 0; i < buffer.size(); i++){
            result += buffer.get(buffer.size()-i-1) + " | ";
            this.stack.push(buffer.get(buffer.size()-i-1));
        }

        return result;
    }
    public String getListInfo(){
        String result = "";
        for (String str: this.list){
            result += str + " | ";
        }
        return result;
    }



}