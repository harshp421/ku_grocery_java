import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class CashierDashboard extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JComboBox<String> productComboBox;
    private JComboBox<String> sizeComboBox;
    private JTextField productIdField;
    private JTextArea productDetailsArea;
    private JSpinner quantitySpinner;
    private LoginPage loginPage;
    private List<Product> productList;
    private List<Product> cart;

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
        categoryComboBox.addItem("Fruits");
        categoryComboBox.addItem("Vegetables");
        categoryComboBox.addItem("Dairy");
        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProductComboBox();
            }
        });
        selectionPanel.add(categoryLabel);
        selectionPanel.add(categoryComboBox);

        // Product ComboBox
        JLabel productLabel = new JLabel("Product:");
        productComboBox = new JComboBox<>();
        productComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProductDetails();
            }
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
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProductToCart();
            }
        });

        // Cart Panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

        DefaultListModel<String> cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Finish Button
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateBill();
            }
        });

        // North panel
        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(800, 50)); // Expl

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome to Cashier Dashboard");
        leftPanel.add(welcomeLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));   
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
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

        // Sample Products (Replace with your data loading logic)
        productList.add(new Product("Apple", "P001", "Fruits", "Large", "Fresh and juicy", 1.5, 100));
        productList.add(new Product("Banana", "P002", "Fruits", "Medium", "Rich in potassium", 1.0, 150));
        productList.add(new Product("Milk", "P003", "Dairy", "1 liter", "Full fat", 2.5, 50));

        // Initialize product comboboxes
        updateProductComboBox();
    }

    private void logout() {
        this.setVisible(false); // Hide the CashierDashboard
        loginPage.setVisible(true); // Show the LoginPage again
    }

    private void updateProductComboBox() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        productComboBox.removeAllItems();
        sizeComboBox.removeAllItems();
        for (Product product : productList) {
            if (product.getCategory().equals(selectedCategory)) {
                productComboBox.addItem(product.getName());
            }
        }
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
                DefaultListModel<String> cartListModel = (DefaultListModel<String>) ((JList<?>) ((JScrollPane) ((BorderLayout) this.getParent().getLayout()).getLayoutComponent(BorderLayout.EAST)).getViewport().getView()).getModel();
                cartListModel.addElement("Product: " + cartProduct.getName() + ", Quantity: " + cartProduct.getStockQuantity() + ", Total: " + cartProduct.getPrice());
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
        DefaultListModel<String> cartListModel = (DefaultListModel<String>) ((JList<?>) ((JScrollPane) ((BorderLayout) this.getParent().getLayout()).getLayoutComponent(BorderLayout.EAST)).getViewport().getView()).getModel();
        cartListModel.clear();
    }
}
