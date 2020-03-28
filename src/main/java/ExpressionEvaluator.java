import java.util.*;
import java.util.function.BiFunction;

public class ExpressionEvaluator {

    public static final char OPEN_BRACKET = '(';
    public static final char CLOSE_BRACKET = ')';
    public static final List<Character> AVAILABLE_OPERATIONS = Arrays.asList('+', '-', '*', '/');

    private static final Map<String, BiFunction<Integer, Integer, Integer>> OPERATION_MAP
            = new HashMap<String, BiFunction<Integer, Integer, Integer>>() {{
        put("+", Integer::sum);
        put("-", (a, b) -> a - b);
        put("*", (a, b) -> a * b);
        put("/", (a, b) -> {
            if (b == 0) {
                throw new IllegalArgumentException("Divide by zero is not allowed!");
            }
            return a / b;
        });
    }};

    private Stack<Integer> values = new Stack<>();
    private Stack<Character> operations = new Stack<>();
    private int inx;

    public int evaluate(String expression) {
        values.clear();
        operations.clear();

        char[] arr = expression.replaceAll(" ", "").toCharArray();

        for (inx = 0; inx < arr.length; ++inx) {
            if (Character.isDigit(arr[inx])) {
                values.push(parseNumber(arr));
            } else if (arr[inx] == OPEN_BRACKET) {
                operations.push(arr[inx]);
            } else if (arr[inx] == CLOSE_BRACKET) {
                performOperations();
                operations.pop();
            } else if (AVAILABLE_OPERATIONS.contains(arr[inx])) {
                while (!operations.empty() && performFirstly(arr[inx], operations.peek())) {
                    values.push(performOperation(operations.pop(), values.pop(), values.pop()));
                }
                operations.push(arr[inx]);
            }
        }

        performOperations();
        return values.pop();
    }

    private Integer parseNumber(char[] arr) {
        StringBuilder builder = new StringBuilder();
        while (inx < arr.length && Character.isDigit(arr[inx])) {
            builder.append(arr[inx++]);
        }
        inx--;
        return Integer.valueOf(builder.toString());
    }

    private boolean performFirstly(char op1, char op2) {
        if (op2 == OPEN_BRACKET || op2 == CLOSE_BRACKET) {
            return false;
        }
        return !(op1 == '*' || op1 == '/') || !(op2 == '+' || op2 == '-');
    }

    private void performOperations() {
        while (!operations.empty() && operations.peek() != OPEN_BRACKET) {
            values.push(performOperation(operations.pop(), values.pop(), values.pop()));
        }
    }

    private int performOperation(char operation, int b, int a) {
        return OPERATION_MAP.getOrDefault(String.valueOf(operation), (p, q) -> 0).apply(a, b);
    }

}
