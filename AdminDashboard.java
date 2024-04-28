import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to KU Grocery");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton manageCashiersButton = new JButton("Manage Cashiers");
        manageCashiersButton.setFont(new Font("Arial", Font.PLAIN, 16));
        manageCashiersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageUserScreen().setVisible(true);
            }
        });
        buttonPanel.add(manageCashiersButton);

        JButton manageProductsButton = new JButton("Manage Products");
        manageProductsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        manageProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageProductScreen().setVisible(true);
            }
        });
        buttonPanel.add(manageProductsButton);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
