package Scientific_calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// UI - Frontend
public class CalculatorUI {
    private JFrame frame;
    private JTextField display;
    private StringBuilder input;
    private ArrayList<String> history;

    public CalculatorUI() {
        frame = new JFrame("Advanced Calculator");
        display = new JTextField();
        input = new StringBuilder();
        history = new ArrayList<>();

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


