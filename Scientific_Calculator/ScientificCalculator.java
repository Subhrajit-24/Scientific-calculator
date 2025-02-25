import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Main Class - Entry Point
public class ScientificCalculator{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorUI::new);
    }
}

// UI - Frontend
class CalculatorUI {
    private JFrame frame;
    private JTextField display;
    private StringBuilder input;
    private ArrayList<String> history;
    private boolean resetInput;

    public CalculatorUI() {
        frame = new JFrame("Advanced Calculator");
        display = new JTextField();
        input = new StringBuilder();
        history = new ArrayList<>();
        resetInput = false;

        setupUI();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/", "4", "5", "6", "*",
                "1", "2", "3", "-", "0", ".", "=", "+",
                "C", "DEL", "(", ")", "HIST", "sin", "cos", "tan",
                "sqrt", "log", "ln", "pow"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Controller - Handles Button Clicks
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            CalculatorController.handleInput(command, display, input, history);
        }
    }
}

// Controller - Business Logic & Backend
class CalculatorController {
    public static void handleInput(String command, JTextField display, StringBuilder input, ArrayList<String> history) {
        try {
            switch (command) {
                case "C":
                    input.setLength(0);
                    display.setText("");
                    break;
                case "DEL":
                    if (input.length() > 0) {
                        input.deleteCharAt(input.length() - 1);
                        display.setText(input.toString());
                    }
                    break;
                case "=":
                    double result = CalculatorBackend.evaluateExpression(input.toString());
                    history.add(input.toString() + " = " + result);
                    display.setText(String.valueOf(result));
                    input.setLength(0);
                    input.append(result);
                    break;
                case "HIST":
                    JOptionPane.showMessageDialog(null, history.isEmpty() ? "No history available" : String.join("\n", history), "History", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "sin": case "cos": case "tan": case "sqrt": case "log": case "ln":
                    double value = Double.parseDouble(display.getText());
                    double resultValue = CalculatorBackend.calculateFunction(command, value);
                    display.setText(String.valueOf(resultValue));
                    input.setLength(0);
                    input.append(resultValue);
                    break;
                case "pow":
                    input.append("^");
                    display.setText(input.toString());
                    break;
                default:
                    input.append(command);
                    display.setText(input.toString());
            }
        } catch (Exception ex) {
            display.setText("Error: " + ex.getMessage());
            input.setLength(0);
        }
    }
}

// Backend - Expression Evaluation & Math Functions
class CalculatorBackend {
    public static double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;
            void nextChar() { ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; }
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
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
