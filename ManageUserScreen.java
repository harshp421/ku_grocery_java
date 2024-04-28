import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManageUserScreen extends JFrame {
    private JTextField usernameField, userIdField, roleField;
    private JButton addButton, updateButton, deleteButton;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUserScreen() {
        setTitle("Manage Users");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Heading label for Manage Users
        JLabel headingLabel = new JLabel("Manage Users");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        add(headingLabel, BorderLayout.NORTH);

        // Panel to hold the user form and buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns with gaps
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // User form fields
        usernameField = new JTextField(20);
        userIdField = new JTextField(20);
        roleField = new JTextField(20);

        // Add form fields to the panel
        leftPanel.add(new JLabel("Username:"));
        leftPanel.add(usernameField);
        leftPanel.add(new JLabel("User ID:"));
        leftPanel.add(userIdField);
        leftPanel.add(new JLabel("Role:"));
        leftPanel.add(roleField);

        // Buttons for add, update, delete operations
        addButton = new JButton("Add User");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        leftPanel.add(addButton);

        updateButton = new JButton("Update User");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        leftPanel.add(updateButton);

        // Panel to hold the user table and delete button
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Table model for user data
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);

        deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        rightPanel.add(deleteButton, BorderLayout.SOUTH);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Load initial user data into the table
        loadUserData();

        // Add list selection listener to the user table
        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = userTable.getSelectedRow();
                    if (selectedRow != -1) {
                        displaySelectedUser(selectedRow);
                    }
                }
            }
        });

        setVisible(true); // Display the frame
    }

    private void loadUserData() {
        String csvFile = "users.csv";
        String line;
        String[] headers = {"Username", "User ID", "Role"};

        // Set table headers
        tableModel.setColumnIdentifiers(headers);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    tableModel.addRow(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading user data from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySelectedUser(int rowIndex) {
        usernameField.setText(tableModel.getValueAt(rowIndex, 0).toString());
        userIdField.setText(tableModel.getValueAt(rowIndex, 1).toString());
        roleField.setText(tableModel.getValueAt(rowIndex, 2).toString());
    }

    private void addUser() {
        String username = usernameField.getText();
        String userId = userIdField.getText();
        String role = roleField.getText();
        User user = new User(username, userId, role);
        user.saveToFile("users.csv");
        tableModel.addRow(new Object[]{username, userId, role});
        clearFields();
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            String username = usernameField.getText();
            String userId = userIdField.getText();
            String role = roleField.getText();

            tableModel.setValueAt(username, selectedRow, 0);
            tableModel.setValueAt(userId, selectedRow, 1);
            tableModel.setValueAt(role, selectedRow, 2);

            try {
                FileWriter writer = new FileWriter("users.csv");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.append(tableModel.getValueAt(i, j).toString());
                        if (j != tableModel.getColumnCount() - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
                writer.close();
                clearFields();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);

            try {
                FileWriter writer = new FileWriter("users.csv");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.append(tableModel.getValueAt(i, j).toString());
                        if (j != tableModel.getColumnCount() - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        userIdField.setText("");
        roleField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageUserScreen();
            }
        });
    }
}
