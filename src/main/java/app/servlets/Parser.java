package app.servlets;


public class Parser {

    private enum Operator {

        PLUS, MINUS, MULTIPLY, DIVIDE, LEFT_BRACKET, RIGHT_BRACKET, LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET, LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET, COMMA, SIN, COS, TAN, COT, SEC, CSC, ASIN, ACOS, ATAN, RANDOM, CEIL, FLOOR, ROUND, ABS, EXP, LOG, SQRT, POW, ATAN2, MIN, MAX, X, NUMBER, UNARY_MINUS, END
    }


    private Node root = null;
    private byte[] expression;
    private double tokenValue;
    private Operator operator;
    private int position;
    private double[] argument;
    private int arguments;

    private class Node {
        Operator operator;
        double value;
        Node left,
                right;

        private void init(Operator operator, double value, Node left) {
            this.operator = operator;
            this.value = value;
            this.left = left;
        }

        Node(Operator operator, Node left) {
            init(operator, 0, left);
        }

        Node(Operator operator) {
            init(operator, 0, null);
        }

        Node(Operator operator, double value) {
            init(operator, value, null);
        }

        double calculate() throws Exception {

            switch (operator) {

                case NUMBER:
                    return value;

                case PLUS:
                    return left.calculate() + right.calculate();

                case MINUS:
                    return left.calculate() - right.calculate();

                case MULTIPLY:
                    return left.calculate() * right.calculate();

                case DIVIDE:
                    return Math.floor(left.calculate() / right.calculate());

                case UNARY_MINUS:
                    return -left.calculate();

                case X:
                    return Math.floor(argument[(int) value]);

                default:
                    return 0;
            }
        }
    }

    private boolean isLetter() {
        return Character.isLetter(expression[position]);
    }

    private boolean isDigit() {
        return Character.isDigit(expression[position]);
    }

    private boolean isPoint() {
        return expression[position] == '.';
    }

    private boolean isFunctionSymbol() {
        byte c = expression[position];
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private void getToken() throws Exception {
        int i;

        if (position == expression.length - 1) {
            operator = Operator.END;
        } else if ((i = "+-*/()[]{},".indexOf(expression[position])) != -1) {
            position++;
            operator = Operator.values()[i];
        } else if (isLetter()) {
            for (i = position++; isFunctionSymbol(); position++) ;
            String token = new String(expression, i, position - i);


            try {
                if (token.charAt(0) == 'X' && token.length() > 1 && Character.isDigit(token.charAt(1))) {
                    i = Integer.parseInt(token.substring(1));

                    if (arguments < i + 1) {
                        arguments = i + 1;
                    }
                    operator = Operator.X;
                    tokenValue = i;
                } else {
                    operator = Operator.valueOf(token);
                    i = operator.ordinal();
                    if (i < Operator.SIN.ordinal() || i > Operator.MAX.ordinal()) {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (IllegalArgumentException _ex) {

            }
        } else if (isDigit() || isPoint()) {
            for (i = position++; isDigit() || isPoint() || expression[position] == 'E'
                    || expression[position - 1] == 'E' && "+-".indexOf(expression[position]) != -1; position++)
                ;

            tokenValue = Double.parseDouble(new String(expression, i, position - i));
            operator = Operator.NUMBER;
        }

    }

    public void compile(String expression) throws Exception {
        position = 0;
        arguments = 0;
        String s = expression.toUpperCase();//for OPERATOR.valueof()

        String from[] = {" ", "\t"};
        for (int i = 0; i < from.length; i++) {
            s = s.replace(from[i], "");
        }
        this.expression = (s + '\0').getBytes();

        getToken();

        root = parse();


    }

    private Node parse() throws Exception {
        Node node = parse1();
        while (operator == Operator.PLUS || operator == Operator.MINUS) {
            node = new Node(operator, node);
            getToken();

            node.right = parse1();
        }
        return node;
    }

    private Node parse1() throws Exception {
        Node node = parse2();
        while (operator == Operator.MULTIPLY || operator == Operator.DIVIDE) {
            node = new Node(operator, node);
            getToken();

            node.right = parse2();
        }
        return node;
    }

    private Node parse2() throws Exception {
        Node node;
        if (operator == Operator.MINUS) {
            getToken();
            node = new Node(Operator.UNARY_MINUS, parse3());
        } else {
            if (operator == Operator.PLUS) {
                getToken();
            }
            node = parse3();
        }
        return node;
    }

    private Node parse3() throws Exception {
        Node node = null;
        Operator open;

        if (operator.ordinal() >= Operator.SIN.ordinal() && operator.ordinal() <= Operator.MAX.ordinal()) {
            int arguments;
            if (operator.ordinal() >= Operator.POW.ordinal() && operator.ordinal() <= Operator.MAX.ordinal()) {
                arguments = 2;
            } else {
                arguments = operator == Operator.RANDOM ? 0 : 1;
            }

            node = new Node(operator);

            getToken();
            open = operator;

            getToken();

            if (arguments > 0) {
                node.left = parse();

                if (arguments == 2) {

                    getToken();
                    node.right = parse();
                }
            }

        } else {
            switch (operator) {

                case X:
                case NUMBER:
                    node = new Node(operator, tokenValue);
                    break;

                case LEFT_BRACKET:
                case LEFT_SQUARE_BRACKET:
                case LEFT_CURLY_BRACKET:
                    open = operator;
                    getToken();
                    node = parse();

                    break;

                default:
                    break;
            }

        }
        getToken();
        return node;
    }

    public double calculate(double[] x) throws Exception {
        this.argument = x;
        return calculate();
    }

    public double calculate() throws Exception {

        return root.calculate();
    }


    public int getArguments() {
        return arguments;
    }

    public static double calculate(String s) throws Exception {
        Parser estimator = new Parser();
        estimator.compile(s);
        estimator.argument = null;//clear all arguments
        return estimator.calculate();
    }

}