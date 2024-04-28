import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManageProductScreen extends JFrame {
    private JTextField nameField, productIdField, categoryField, sizeField, descriptionField, priceField, stockQuantityField;
    private JButton addButton, updateButton, deleteButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ManageProductScreen() {
        setTitle("Manage Products");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Heading label for Manage Products
        JLabel headingLabel = new JLabel("Manage Products");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        add(headingLabel, BorderLayout.NORTH);

        // Panel to hold the product form and buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 2, 10, 10)); // 8 rows, 2 columns with gaps
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Product form fields
        nameField = new JTextField(20);
        productIdField = new JTextField(20);
        categoryField = new JTextField(20);
        sizeField = new JTextField(20);
        descriptionField = new JTextField(20);
        priceField = new JTextField(20);
        stockQuantityField = new JTextField(20);

        // Add form fields to the panel
        leftPanel.add(new JLabel("Name:"));
        leftPanel.add(nameField);
        leftPanel.add(new JLabel("Product ID:"));
        leftPanel.add(productIdField);
        leftPanel.add(new JLabel("Category:"));
        leftPanel.add(categoryField);
        leftPanel.add(new JLabel("Size:"));
        leftPanel.add(sizeField);
        leftPanel.add(new JLabel("Description:"));
        leftPanel.add(descriptionField);
        leftPanel.add(new JLabel("Price:"));
        leftPanel.add(priceField);
        leftPanel.add(new JLabel("Stock Quantity:"));
        leftPanel.add(stockQuantityField);

        // Buttons for add, update, delete operations
        addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        leftPanel.add(addButton);

        updateButton = new JButton("Update Product");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });
        leftPanel.add(updateButton);

        // Panel to hold the product table and delete button
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Table model for product data
        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);

        deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });
        rightPanel.add(deleteButton, BorderLayout.SOUTH);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Load initial product data into the table
        loadProductData();

        setVisible(true); // Display the frame
    }

    private void loadProductData() {
        String csvFile = "products.csv";
        String line;
        String[] headers = {"Name", "Product ID", "Category", "Size", "Description", "Price", "Stock Quantity"};

        // Set table headers
        tableModel.setColumnIdentifiers(headers);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    tableModel.addRow(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading product data from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        String name = nameField.getText();
        String productId = productIdField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String description = descriptionField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stockQuantity = Integer.parseInt(stockQuantityField.getText());

        // Validate input fields
        if (name.isEmpty() || productId.isEmpty() || category.isEmpty() || size.isEmpty() || description.isEmpty() || priceField.getText().isEmpty() || stockQuantityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add product to table
        String[] rowData = {name, productId, category, size, description, String.valueOf(price), String.valueOf(stockQuantity)};
        tableModel.addRow(rowData);

        // Clear input fields after adding
        clearFields();

        // Save data to file
        saveDataToFile();
    }

  private void updateProduct() {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow != -1) {
        String name = nameField.getText();
        String productId = productIdField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String description = descriptionField.getText();
        String priceText = priceField.getText();
        String stockQuantityText = stockQuantityField.getText();

        // Validate input fields
        if (name.isEmpty() || productId.isEmpty() || category.isEmpty() || size.isEmpty() || description.isEmpty() || priceText.isEmpty() || stockQuantityText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate price and stock quantity fields
        try {
            double price = Double.parseDouble(priceText);
            int stockQuantity = Integer.parseInt(stockQuantityText);

            // Update selected row in table
            productTable.setValueAt(name, selectedRow, 0);
            productTable.setValueAt(productId, selectedRow, 1);
            productTable.setValueAt(category, selectedRow, 2);
            productTable.setValueAt(size, selectedRow, 3);
            productTable.setValueAt(description, selectedRow, 4);
            productTable.setValueAt(price, selectedRow, 5);
            productTable.setValueAt(stockQuantity, selectedRow, 6);

            // Clear input fields after updating
            clearFields();

            // Save data to file
            saveDataToFile();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid values for price and stock quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Please select a product to edit.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);

            // Save data to file
            saveDataToFile();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDataToFile() {
        String csvFile = "products.csv";
        try (FileWriter writer = new FileWriter(csvFile)) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.append(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving product data to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        productIdField.setText("");
        categoryField.setText("");
        sizeField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        stockQuantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageProductScreen());
    }
}