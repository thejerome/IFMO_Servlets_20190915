package main;

import java.util.*;

class Node {
    private String value;
    private Node left;
    private Node right;

    Node(String value) {
        this.value = value;
        this.left = this.right = null;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return this.value;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }
}

public class Calculator {
    private Integer result = null;
    private Node root = null;
    private String infix = null;
    private List<String> postfix = null;

    private boolean isOperator(String c) {
        return "+".equals(c) || "-".equals(c) || "/".equals(c) ||"*".equals(c);
    }

    public void constructTree() {
        if (root == null) {
            if (postfix == null) {
                postfix = toPostfix(infix);
            }
            Stack<Node> stack = new Stack<Node>();
            Node t;
            Node t1;
            Node t2;
            for (String c : postfix) {
                if (!isOperator(c)) {
                    t = new Node(c);
                    stack.push(t);
                } else {
                    t = new Node(c);
                    t1 = stack.pop();
                    t2 = stack.pop();
                    t.setRight(t1);
                    t.setLeft(t2);
                    stack.push(t);
                }
            }

            t = stack.peek();
            stack.pop();
            root = t;
        }
    }

    public int getResult() {
        if (result == null) {
            result = eval(root);
        }
        return result;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    private int eval(Node t) {
        if (t == null) {
            return 0;
        }
        if (t.getLeft() == null && t.getRight() == null) {
            return toInt(t.getValue());
        }

        int l = eval(t.getLeft());
        int r = eval(t.getRight());
        if ("+".equals(t.getValue())) {
            return l + r;
        }
        if ("-".equals(t.getValue())) {
            return l - r;
        }
        if ("/".equals(t.getValue())) {
            return l / r;
        }
        return l * r;
    }

    private List<String> toPostfix(String infix) {
        final String delimeter = "(+-/*)";
        StringTokenizer stringTokenizer = new StringTokenizer(infix, delimeter, true);
        List<String> res = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        String sign = "";
        while (stringTokenizer.hasMoreTokens()) {
            String element = stringTokenizer.nextToken();
            if (delimeter.contains(element)) {
                if ("(".equals(element)) {
                    stack.push(element);
                } else if (")".equals(element)) {
                    while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                        res.add(stack.pop());
                    }
                    stack.pop();
                } else if ("-".equals(element) && ((stack.isEmpty() || "(".equals(stack.peek())) && (res.isEmpty() || delimeter.contains(res.get(res.size() - 1))))) {
                    sign = element;
                } else {
                    while (!stack.isEmpty() && prec(element.charAt(0)) <= prec(stack.peek().charAt(0))) {
                        res.add(stack.pop());
                    }
                    stack.push(element);
                }
            } else {
                res.add(sign + element);
                sign = "";
            }
        }

        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }

        return res;
    }

    private int prec(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private int toInt(String s) {
        return Integer.parseInt(s);
    }
}