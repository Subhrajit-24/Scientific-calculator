package Scientific_calculator;

// Backend - Expression Evaluation & Math Functions
public class CalculatorBackend {
    public static double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                double x;
                int startPos = this.pos;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch == '(') {
                    nextChar();
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing closing parenthesis");
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }

    public static double calculateFunction(String function, double value) {
        switch (function) {
            case "sin": return Math.sin(Math.toRadians(value));
            case "cos": return Math.cos(Math.toRadians(value));
            case "tan": return Math.tan(Math.toRadians(value));
            case "sqrt": if (value < 0) throw new IllegalArgumentException("Cannot take square root of a negative number"); return Math.sqrt(value);
            case "log": return Math.log10(value);
            case "ln": return Math.log(value);
            default: throw new IllegalArgumentException("Unknown function: " + function);
 }
}
}

