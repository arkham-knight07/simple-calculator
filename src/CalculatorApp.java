import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Calculator with clean UI and basic arithmetic operations
 * Built using Java Swing/AWT for CS project
 * @version 1.0
 */
public class CalculatorApp extends JFrame implements ActionListener {
    private final JTextField display;
    private String current = "";
    private String operator = "";
    private double result = 0;
    private boolean startNewNumber = true;

    public CalculatorApp() {
        // Main window setup
        setTitle("Simple Calculator");
        setSize(360, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(0xF2F2F7));

        // Top header
        JLabel header = new JLabel("Simple Calculator", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(0x1E88E5));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(header, BorderLayout.NORTH);

        // Display panel for results
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Consolas", Font.BOLD, 36));
        display.setBackground(new Color(0xFFFFFF));
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                BorderFactory.createLineBorder(new Color(0xD0D7DE), 2)
        ));
        display.setPreferredSize(new Dimension(0, 80));
        add(display, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        String[] buttons = {
                "C", "←", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };

        for (String b : buttons) {
            JButton btn = createButton(b);
            buttonsPanel.add(btn);
        }

        add(buttonsPanel, BorderLayout.SOUTH);

        // Use system look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setResizable(false);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(new Color(0x333333));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xC7CDD3)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        if (text.matches("[0-9]")) {
            btn.setBackground(new Color(0xFFFFFF));
        } else if (text.equals("C") || text.equals("←")) {
            btn.setBackground(new Color(0xFFCDD2));
            btn.setForeground(new Color(0x660000));
        } else if (text.equals("=") ) {
            btn.setBackground(new Color(0x43A047));
            btn.setForeground(Color.WHITE);
        } else if (text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/")) {
            // Changed color of arithmetic operators from blue to orange
            btn.setBackground(new Color(0xFF9800));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(0xECEFF1));
        }

        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.matches("[0-9]")) {
            numberPressed(cmd);
        } else if (cmd.equals(".")) {
            dotPressed();
        } else if (cmd.equals("C")) {
            clearAll();
        } else if (cmd.equals("←")) {
            backspace();
        } else if (cmd.equals("±")) {
            negate();
        } else if (cmd.equals("%")) {
            percent();
        } else if (cmd.equals("=") ) {
            calculate();
            operator = "";
            startNewNumber = true;
        } else { // operator
            operatorPressed(cmd);
        }
    }

    // Handle number button presses
    private void numberPressed(String digit) {
        if (startNewNumber) {
            current = digit.equals("0") ? "0" : digit;
            startNewNumber = false;
        } else {
            if (current.equals("0") && digit.equals("0")) return;
            current = current.equals("0") ? digit : current + digit;
        }
        display.setText(current);
    }

    // Handle decimal point button
    private void dotPressed() {
        if (startNewNumber) {
            current = "0.";
            startNewNumber = false;
        } else if (!current.contains(".")) {
            current += ".";
        }
        display.setText(current);
    }

    // Reset calculator state
    private void clearAll() {
        current = "";
        operator = "";
        result = 0;
        startNewNumber = true;
        display.setText("0");
    }

    // Remove last digit
    private void backspace() {
        if (startNewNumber || current.isEmpty()) return;
        current = current.length() <= 1 ? "" : current.substring(0, current.length() - 1);
        display.setText(current.isEmpty() ? "0" : current);
    }

    // Change sign of current number
    private void negate() {
        if (current.isEmpty()) return;
        if (current.startsWith("-")) current = current.substring(1);
        else current = "-" + current;
        display.setText(current);
    }

    // Convert to percentage
    private void percent() {
        if (current.isEmpty()) return;
        try {
            double val = Double.parseDouble(current);
            val = val / 100.0;
            current = stripTrailingZeros(val);
            display.setText(current);
        } catch (NumberFormatException ex) {
            display.setText("Error");
            current = "";
        }
    }

    // Process arithmetic operators
    private void operatorPressed(String op) {
        if (!current.isEmpty()) {
            if (!operator.isEmpty()) {
                calculate();
            } else {
                result = Double.parseDouble(current);
            }
        }
        operator = op;
        startNewNumber = true;
    }

    // Perform arithmetic calculation
    private void calculate() {
        if (operator.isEmpty() && !current.isEmpty()) {
            try {
                result = Double.parseDouble(current);
                display.setText(stripTrailingZeros(result));
                current = stripTrailingZeros(result);
            } catch (NumberFormatException ex) { display.setText("Error"); }
            return;
        }

        if (current.isEmpty()) return;
        try {
            double right = Double.parseDouble(current);
            switch (operator) {
                case "+": result = result + right; break;
                case "-": result = result - right; break;
                case "*": result = result * right; break;
                case "/": result = right == 0 ? Double.NaN : result / right; break;
                default: break;
            }
            current = stripTrailingZeros(result);
            display.setText(current);
        } catch (NumberFormatException ex) {
            display.setText("Error");
            current = "";
        }
    }

    // Format number output to remove unnecessary decimal zeros
    private String stripTrailingZeros(double val) {
        if (Double.isNaN(val)) return "Error";
        if (val == (long) val) return String.format("%d", (long) val);
        return String.format("%s", val);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorApp::new);
    }
}
