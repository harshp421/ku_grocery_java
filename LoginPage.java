import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends Frame {
    private Label userIdLabel, passwordLabel;
    private TextField userIdField;
    private TextField passwordField;
    private Button loginButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(400, 300);
        setLayout(null); // Using absolute positioning

        // Main heading for KU Grocery
        Label mainHeading = new Label("KU Grocery");
        mainHeading.setFont(new Font("Arial", Font.BOLD, 24));
        mainHeading.setForeground(Color.GREEN);
        mainHeading.setBounds(120, 30, 200, 30);
        add(mainHeading);

        // Subheading for LOGIN
        Label loginHeading = new Label("LOGIN");
        loginHeading.setFont(new Font("Arial", Font.BOLD, 18));
        loginHeading.setForeground(Color.GREEN);
        loginHeading.setBounds(180, 60, 80, 30);
        add(loginHeading);

        // Create labels
        userIdLabel = new Label("User ID:");
        userIdLabel.setBounds(50, 100, 80, 30);
        add(userIdLabel);

        passwordLabel = new Label("Password:");
        passwordLabel.setBounds(50, 150, 80, 30);
        add(passwordLabel);

        // Create text fields
        userIdField = new TextField();
        userIdField.setBounds(150, 100, 200, 30);
        add(userIdField);

        passwordField = new TextField();
        passwordField.setBounds(150, 150, 200, 30);
        passwordField.setEchoChar('*');
        add(passwordField);

        // Create login button
        loginButton = new Button("Login");
        loginButton.setBounds(150, 200, 100, 30);
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = passwordField.getText();
                if (userId.equals("Salim") && password.equals("Salim1234")) {
                    // Admin login
                    new AdminDashboard().setVisible(true);
                    setVisible(false);
                    return;
                }
                boolean authenticated = authenticateUser(userId, password);
                if (authenticated) {
                    // Cashier login
                    new CashierDashboard().setVisible(true);
                    setVisible(false);
                } else {
                    showMessageDialog("Invalid User ID or Password!");
                }
            }
        });
        add(loginButton);
        setBackground(Color.WHITE);

        // Center the frame on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

        // Handle window close event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose(); // Close the window
            }
        });

        setVisible(true); // Display the frame
    }

    private boolean authenticateUser(String userId, String password) {
        String csvFile = "users.csv";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String csvUserId = userData[1].trim();
                    String csvPassword = userData[2].trim();

                    if (csvUserId.equals(userId) && csvPassword.equals(password)) {
                        return true; // Match found, authentication successful
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle file IO exception
        }
        return false; // No matching user found or error occurred
    }

    private void showMessageDialog(String message) {
        // Display a message dialog
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 100);
        Label label = new Label(message);
        dialog.add(label);
        dialog.setLocationRelativeTo(this);
        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog
            }
        });
        okButton.setBackground(Color.GREEN);
        okButton.setForeground(Color.WHITE);
        dialog.add(okButton);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
