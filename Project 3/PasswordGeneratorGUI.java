import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PasswordGeneratorGUI extends JFrame {
    // GUI Components
    private JTextField memorableField;
    private JTextField lengthField;
    private JTextField passwordField;
    private JButton generateButton;
    private JButton clearButton;
    private JButton copyButton;
    private JButton saveButton;

    public PasswordGeneratorGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("EC-Based Strong Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Add components
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createConfigPanel(), BorderLayout.CENTER);
        mainPanel.add(createOutputPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel titleLabel = new JLabel("EC-Based Strong Password Generator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Generate cryptographically strong passwords using Evolutionary Computing");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return headerPanel;
    }

    private JPanel createConfigPanel() {
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setBorder(BorderFactory.createTitledBorder("Password Configuration"));
        configPanel.setBackground(Color.WHITE);

        // Memorable Password
        JPanel memorablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memorablePanel.setBackground(Color.WHITE);
        JLabel memorableLabel = new JLabel("Memorable Password:");
        memorableLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        memorableField = new JTextField(20);
        memorablePanel.add(memorableLabel);
        memorablePanel.add(memorableField);

        // Desired Length
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lengthPanel.setBackground(Color.WHITE);
        JLabel lengthLabel = new JLabel("Desired Length:");
        lengthLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        lengthField = new JTextField(5);
        lengthField.setText("16");
        lengthField.setHorizontalAlignment(JTextField.CENTER);
        JLabel lengthNote = new JLabel("(8-32)");
        lengthNote.setFont(new Font("Arial", Font.PLAIN, 10));
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthField);
        lengthPanel.add(lengthNote);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        generateButton = new JButton("Generate Password");
        generateButton.setFont(new Font("Arial", Font.BOLD, 10));
        generateButton.setBackground(new Color(52, 152, 219));
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(e -> generatePassword());
        
        clearButton = new JButton("Clear All");
        clearButton.setFont(new Font("Arial", Font.BOLD, 10));
        clearButton.setBackground(new Color(231, 76, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearAll());
        
        buttonPanel.add(generateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(clearButton);

        configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        configPanel.add(memorablePanel);
        configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        configPanel.add(lengthPanel);
        configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        configPanel.add(buttonPanel);
        configPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return configPanel;
    }

    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Generated Strong Password"));
        outputPanel.setBackground(Color.WHITE);

        // Password Display
        passwordField = new JTextField();
        passwordField.setFont(new Font("Courier", Font.BOLD, 12));
        passwordField.setForeground(new Color(44, 62, 80));
        passwordField.setEditable(false);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        
        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Arial", Font.BOLD, 10));
        copyButton.setBackground(new Color(52, 152, 219));
        copyButton.setForeground(Color.WHITE);
        copyButton.addActionListener(e -> copyToClipboard());
        
        saveButton = new JButton("Save to File");
        saveButton.setFont(new Font("Arial", Font.BOLD, 10));
        saveButton.setBackground(new Color(52, 152, 219));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveToFile());

        actionPanel.add(copyButton);
        actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        actionPanel.add(saveButton);

        outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        outputPanel.add(passwordField);
        outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        outputPanel.add(actionPanel);
        outputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return outputPanel;
    }

    // EC Functions - Translated from Python
    private double ecFormula(double row) {
        return 1.148 * (0.00720794 * (Math.ceil(Math.abs(Math.tan((-0.976665) - 0.056824 * row) - 4.3663)) - 9.43194) + 
               fmod(fmod(6.09373, (-3.89202) + row) * (row + 0.0265342) + 0.199477, -0.909621));
    }

    private double fmod(double a, double b) {
        // Java doesn't have fmod like Python, so we implement it
        return a % b;
    }

    private List<Double> generateRandomNumberStream(double seed, int length) {
        List<Double> stream = new ArrayList<>();
        stream.add(seed);
        for (int i = 1; i < length; i++) {
            stream.add(ecFormula(stream.get(i - 1)));
        }
        return stream;
    }

    private String generateEcStrongPassword(String memorablePassword, int length) {
        if (length < memorablePassword.length()) {
            throw new IllegalArgumentException("Length must be at least the same as memorable password");
        }

        // Convert memorable password to numerical seed
        double seed = 0;
        for (char c : memorablePassword.toCharArray()) {
            seed += (int) c / 256.0;
        }

        // Generate random stream using EC function
        List<Double> randomStream = generateRandomNumberStream(seed, length);
        
        // Transform to uniform distribution
        List<Double> uniformStream = transformToUniform(randomStream);
        
        // Convert to printable ASCII characters (33-126)
        StringBuilder strongPassword = new StringBuilder();
        for (double value : uniformStream) {
            int asciiValue = (int) (Math.abs(value * 1000) % 94) + 33;
            strongPassword.append((char) asciiValue);
        }
        
        return strongPassword.toString();
    }

    private List<Double> transformToUniform(List<Double> inputArray) {
        // Create a copy for ranking
        List<Double> sorted = new ArrayList<>(inputArray);
        Collections.sort(sorted);
        
        // Calculate ranks (averaging ties)
        List<Double> ranks = new ArrayList<>();
        for (double value : inputArray) {
            int firstIndex = sorted.indexOf(value);
            int lastIndex = sorted.lastIndexOf(value);
            double rank = (firstIndex + lastIndex) / 2.0 + 1;
            ranks.add(rank);
        }
        
        // Normalize to [0, 1]
        List<Double> uniform = new ArrayList<>();
        int n = inputArray.size();
        for (double rank : ranks) {
            uniform.add((rank - 1) / (n - 1));
        }
        
        return uniform;
    }

    // Event Handlers
    private void generatePassword() {
        String memorablePassword = memorableField.getText().trim();
        String lengthStr = lengthField.getText().trim();

        if (memorablePassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a memorable password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (lengthStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password length", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int length = Integer.parseInt(lengthStr);
            if (length < 8 || length > 32) {
                JOptionPane.showMessageDialog(this, "Password length must be between 8 and 32 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show loading state
            generateButton.setEnabled(false);
            
            String strongPassword = generateEcStrongPassword(memorablePassword, length);
            passwordField.setText(strongPassword);
            JOptionPane.showMessageDialog(this, "Strong password generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for password length", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            generateButton.setEnabled(true);
        }
    }

    private void copyToClipboard() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No password to copy. Please generate a password first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringSelection stringSelection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(this, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveToFile() {
        String password = passwordField.getText();
        String memorablePassword = memorableField.getText();

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No password to save. Please generate a password first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Strong Password");
        fileChooser.setSelectedFile(new java.io.File("strong_password.txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                writer.println("EC-Based Strong Password Generator");
                writer.println(new String(new char[40]).replace('\0', '='));
                writer.println("Memorable password: " + memorablePassword);
                writer.println("Strong password: " + password);
                writer.println("Length: " + password.length());
                writer.println("\nGenerated using Evolutionary Computing Cipher");
                
                JOptionPane.showMessageDialog(this, "Password saved to " + fileChooser.getSelectedFile().getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAll() {
        memorableField.setText("");
        lengthField.setText("16");
        passwordField.setText("");
        memorableField.requestFocus();
        JOptionPane.showMessageDialog(this, "All fields have been cleared!", "Cleared", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new PasswordGeneratorGUI().setVisible(true);
        });
    }
}