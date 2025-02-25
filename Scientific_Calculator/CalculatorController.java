package Scientific_calculator;

import javax.swing.*;
import java.util.ArrayList;

// Controller - Business Logic & Backend
public class CalculatorController {
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

