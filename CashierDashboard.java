import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class CashierDashboard extends JFrame {

    private final JComboBox<String> categoryComboBox;
    private final JComboBox<String> productComboBox;
    private final JComboBox<String> sizeComboBox;
    private final JTextField productIdField;
    private final JTextArea productDetailsArea;
    private final JSpinner quantitySpinner;
    private final LoginPage loginPage;
    private final List<Product> productList;
    private final List<Product> cart;
    private final DefaultListModel<String> cartListModel;

    public CashierDashboard(LoginPage loginPage) {
        this.loginPage = loginPage;
        setTitle("Cashier Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Product Selection Panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Product Selection"));

        // Category ComboBox
        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Stationary");
        categoryComboBox.addItem("Sweets");
        categoryComboBox.addItem("Drinks");
        categoryComboBox.addActionListener((ActionEvent e) -> {
            updateProductComboBox();
        });
        selectionPanel.add(categoryLabel);
        selectionPanel.add(categoryComboBox);

        // Product ComboBox
        JLabel productLabel = new JLabel("Product:");
        productComboBox = new JComboBox<>();
        productComboBox.addActionListener((ActionEvent e) -> {
            updateProductDetails();
        });
        selectionPanel.add(productLabel);
        selectionPanel.add(productComboBox);

        // Size ComboBox
        JLabel sizeLabel = new JLabel("Size:");
        sizeComboBox = new JComboBox<>();
        selectionPanel.add(sizeLabel);
        selectionPanel.add(sizeComboBox);

        // Product ID Field
        JLabel productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField(10);
        selectionPanel.add(productIdLabel);
        selectionPanel.add(productIdField);

        // Product Details Area
        productDetailsArea = new JTextArea(10, 40);
        productDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(productDetailsArea);

        // Quantity Spinner
        JLabel quantityLabel = new JLabel("Quantity:");
        SpinnerModel quantityModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(quantityModel);

        // Add Product Button
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener((ActionEvent e) -> {
            addProductToCart();
        });

        // Cart Panel
       

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        
        cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        // DefaultListModel<String> cartListModel = new DefaultListModel<>();
        // JList<String> cartList = new JList<>(cartListModel);
        // JScrollPane cartScrollPane = new JScrollPane(cartList);
        // cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Finish Button
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener((ActionEvent e) -> {
            generateBill();
        });

        // North panel
        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(800, 50)); // Expl

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome to Cashier Dashboard");
        leftPanel.add(welcomeLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener((ActionEvent e) -> {
            logout();
        });

        rightPanel.add(logoutButton);

        // Add sub-panels to northPanel
        northPanel.add(leftPanel, BorderLayout.WEST);
        northPanel.add(rightPanel, BorderLayout.EAST);

        JPanel combinedNorthPanel = new JPanel();
        combinedNorthPanel.setLayout(new BoxLayout(combinedNorthPanel, BoxLayout.Y_AXIS));
        combinedNorthPanel.add(northPanel);
        combinedNorthPanel.add(selectionPanel);
        // Add Components to Main Panel
        mainPanel.add(detailsScrollPane, BorderLayout.CENTER);
        mainPanel.add(quantityLabel, BorderLayout.WEST);
        mainPanel.add(quantitySpinner, BorderLayout.WEST);
        mainPanel.add(addButton, BorderLayout.WEST);
        mainPanel.add(cartPanel, BorderLayout.EAST);
        mainPanel.add(finishButton, BorderLayout.SOUTH);
        mainPanel.add(combinedNorthPanel, BorderLayout.NORTH);

        add(mainPanel);

        validate();
        repaint();

        setVisible(true);

        // Initialize product list and cart
        productList = new ArrayList<>();
        cart = new ArrayList<>();

        loadProductData();
    }

    private void logout() {
        this.setVisible(false); // Hide the CashierDashboard
        loginPage.setVisible(true); // Show the LoginPage again
    }

    private void updateProductComboBox() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        productComboBox.removeAllItems();
        sizeComboBox.removeAllItems();
        productIdField.setText(""); 
        for (Product product : productList) {
            if (product.getCategory().equals(selectedCategory)) {
                productComboBox.addItem(product.getName());
            }
        }
        // Add an action listener to update size and product ID when a product is selected
        productComboBox.addActionListener(e -> {
            updateSizeAndProductId();
        });
    }

    private void updateSizeAndProductId() {
        String selectedProductName = (String) productComboBox.getSelectedItem();
        if (selectedProductName != null) {
            for (Product product : productList) {
                if (product.getName().equals(selectedProductName)) {
                    sizeComboBox.addItem(product.getSize());  // Populate size combo box
                    productIdField.setText(product.getProductId());  // Set product ID
                    break;  // Assuming only one product with a unique name, break after found
                }
            }
        }
    }

    private void loadProductData() {
        String csvFile = "products.csv";
        String line;
        productList.clear(); // Clear existing product list

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the header line if your CSV has headers
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Check if the data has the expected number of elements
                if (data.length == 7) {
                    String name = data[0];
                    String productId = data[1];
                    String category = data[2];
                    String size = data[3];
                    String description = data[4];
                    double price = Double.parseDouble(data[5]);
                    int stockQuantity = Integer.parseInt(data[6]);

                    // Add product to the list
                    productList.add(new Product(name, productId, category, size, description, price, stockQuantity));
                } else {
                    // Log or display a message for unexpected data
                    System.err.println("Skipping invalid data: " + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading product data from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // After loading, you might need to update your product combo boxes
        updateProductComboBox();
    }

    private void updateProductDetails() {
        String selectedProductName = (String) productComboBox.getSelectedItem();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String selectedSize = (String) sizeComboBox.getSelectedItem();
        String selectedProductId = productIdField.getText().trim();

        for (Product product : productList) {
            if (product.getCategory().equals(selectedCategory)
                    && product.getName().equals(selectedProductName)
                    && product.getSize().equals(selectedSize)
                    && product.getProductId().equals(selectedProductId)) {
                String details = "Product ID: " + product.getProductId() + "\n"
                        + "Category: " + product.getCategory() + "\n"
                        + "Size: " + product.getSize() + "\n"
                        + "Description: " + product.getDescription() + "\n"
                        + "Price: $" + product.getPrice() + "\n"
                        + "Stock Quantity: " + product.getStockQuantity();
                productDetailsArea.setText(details);
                return;
            }
        }
        productDetailsArea.setText("Product not found.");
    }

    private void addProductToCart() {
        String selectedProductName = (String) productComboBox.getSelectedItem();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String selectedSize = (String) sizeComboBox.getSelectedItem();
        String selectedProductId = productIdField.getText().trim();
        int quantity = (int) quantitySpinner.getValue();
    
        for (Product product : productList) {
            if (product.getCategory().equals(selectedCategory)
                    && product.getName().equals(selectedProductName)
                    && product.getSize().equals(selectedSize)
                    && product.getProductId().equals(selectedProductId)) {
                Product cartProduct = new Product(product.getName(), product.getProductId(), product.getCategory(),
                        product.getSize(), product.getDescription(), product.getPrice(), quantity);
                cart.add(cartProduct);
                cartListModel.addElement("Product: " + cartProduct.getName() + ", Quantity: " + cartProduct.getStockQuantity() + ", Total: " + cartProduct.getPrice() * quantity);
                return;
            }
        }
        productDetailsArea.setText("Product not found.");
    }
    

    private void generateBill() {
        double totalAmount = 0;
        StringBuilder billDetails = new StringBuilder();
        billDetails.append("Bill Details:\n");
        billDetails.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        billDetails.append("Cashier: John Doe\n");
        billDetails.append("--------------------------------------------------\n");
        for (Product product : cart) {
            billDetails.append("Product ID: ").append(product.getProductId()).append("\n");
            billDetails.append("Name: ").append(product.getName()).append("\n");
            billDetails.append("Size: ").append(product.getSize()).append("\n");
            billDetails.append("Quantity: ").append(product.getStockQuantity()).append("\n");
            double totalPrice = product.getPrice() * product.getStockQuantity();
            billDetails.append("Price: $").append(totalPrice).append("\n");
            billDetails.append("--------------------------------------------------\n");
            totalAmount += totalPrice;
        }
        double vat = totalAmount * 0.1; // Assuming 10% VAT
        double finalAmount = totalAmount + vat;
    
        billDetails.append("Total Price: $").append(totalAmount).append("\n");
        billDetails.append("VAT (10%): $").append(vat).append("\n");
        billDetails.append("--------------------------------------------------\n");
        billDetails.append("Total Amount (including VAT): $").append(finalAmount).append("\n");
        billDetails.append("--------------------------------------------------\n");
        billDetails.append("Thank you for shopping with us!");
    
        JOptionPane.showMessageDialog(this, billDetails.toString(), "Bill", JOptionPane.INFORMATION_MESSAGE);
    
        // Clear cart after generating bill
        cart.clear();
        cartListModel.clear(); // Use direct reference to clear the model
    }
    
}
