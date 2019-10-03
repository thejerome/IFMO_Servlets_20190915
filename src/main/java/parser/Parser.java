package parser;

import java.util.Map;


public class Parser {
    public static int parse(String expression, Map<String, Object> vars) {
        return RPNSolver.parse(expression, vars);
    }
}
