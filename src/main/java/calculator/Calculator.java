package calculator;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

class Node {
    private String value;
    private Node left;
    private Node right;

    Node(String v) {
        value = v;
        left = right = null;
    }

    public void setLeft(Node left) {
        this.left = left;
    }


    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}
public class Calculator {

    private Integer result = null;
    private Node _root = null;
    private String _infix = null;
    private List<String> _postfix = null;

    private boolean isOperator(String c) {
        return "+".equals(c) || "-".equals(c) || "*".equals(c)|| "/".equals(c);
    }


    public void constructTree() {
        if(_root == null) {
            if(_postfix == null) {
                _postfix = toPostfix(_infix);
            }
            Stack<Node> st = new Stack<>();
            Node t;
            Node t1;
            Node t2;

            for (String c: _postfix) {
                if(!isOperator(c)) {
                    t = new Node(c);
                    st.push(t);
                } else {
                    t = new Node(c);
                    t1 = st.pop();
                    t2 = st.pop();
                    t.setRight(t1);
                    t.setLeft(t2);
                    st.push(t);
                }
            }
            t = st.peek();
            st.pop();
            _root = t;

        }
    }

    public int getResult() {
        if(result == null) {
            result = eval(_root);
        }
        return result;
    }

    public void setInfix(String expression) {
        this._infix = expression;
    }

    private int eval(Node root) {
        if (root == null) {
            return 0;
        }

        if(root.getLeft() == null && root.getRight() == null ) {
            return toInt(String.valueOf(root.getValue()));
        }

        int l_val = eval(root.getLeft());
        int r_val = eval(root.getRight());
        if("+".equals(root.getValue())) {
            return l_val + r_val;
        } else if("*".equals(root.getValue())) {
            return l_val * r_val;
        } else if("-".equals(root.getValue())) {
            return l_val - r_val;
        }
        else return l_val / r_val;

    }

    private List<String> toPostfix(String infix) {
        final String delim = "(+-*/)";
        StringTokenizer tokenizer = new StringTokenizer(infix, delim, true);
        List<String> postfix = new ArrayList<>();
        Stack<String> st = new Stack<>();
        String sign = "";
        while (tokenizer.hasMoreTokens()) {
            String el = tokenizer.nextToken();
            if(delim.contains(el)) {
                if("(".equals(el)) {
                    st.push(el);
                } else if(")".equals(el)) {
                    while(!st.isEmpty() && !"(".equals(st.peek())) {
                        postfix.add(st.pop());
                    }
                    st.pop();
                } else if("-".equals(el) && ( (st.isEmpty() || "(".equals(st.peek())) && (postfix.isEmpty() || delim.contains(postfix.get(postfix.size()-1))) )) {
                    sign = el;
                } else {
                    while(!st.isEmpty() && prec(el.charAt(0)) <= prec(st.peek().charAt(0))) {
                        postfix.add(st.pop());
                    }
                    st.push(el);
                }
            } else {
                postfix.add(sign + el);
                sign = "";
            }
        }

        while (!st.isEmpty()) {
            postfix.add(st.pop());
        }

        return postfix;
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