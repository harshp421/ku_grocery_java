import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Product {
    private String name;
    private String productId;
    private String category;
    private String size;
    private String description;
    private double price;
    private int stockQuantity;

    public Product(String name, String productId, String category, String size, String description, double price, int stockQuantity) {
        this.name = name;
        this.productId = productId;
        this.category = category;
        this.size = size;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    //add getter and 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String productLine = String.format("%s,%s,%s,%s,%s,%.2f,%d\n",
                    name, productId, category, size, description, price, stockQuantity);
            writer.write(productLine);
            JOptionPane.showMessageDialog(null, "Product details saved to file: " + filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while saving product details to file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update an existing product in the CSV file
    public static void updateProduct(String filename, String productId, Product updatedProduct) {
        List<Product> products = new ArrayList<>();

        // Read existing products from the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String name = parts[0];
                    String existingProductId = parts[1];
                    String category = parts[2];
                    String size = parts[3];
                    String description = parts[4];
                    double price = Double.parseDouble(parts[5]);
                    int stockQuantity = Integer.parseInt(parts[6]);

                    // Create Product object from CSV data
                    Product product = new Product(name, existingProductId, category, size, description, price, stockQuantity);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while reading product details from file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the product with the specified productId
        boolean found = false;
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                // Update product attributes
                product.setName(updatedProduct.getName());
                product.setCategory(updatedProduct.getCategory());
                product.setSize(updatedProduct.getSize());
                product.setDescription(updatedProduct.getDescription());
                product.setPrice(updatedProduct.getPrice());
                product.setStockQuantity(updatedProduct.getStockQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Product with productId '" + productId + "' not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Write updated products back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products) {
                String productLine = String.format("%s,%s,%s,%s,%s,%.2f,%d\n",
                        product.getName(), product.getProductId(), product.getCategory(),
                        product.getSize(), product.getDescription(), product.getPrice(), product.getStockQuantity());
                writer.write(productLine);
            }
            JOptionPane.showMessageDialog(null, "Product with productId '" + productId + "' updated successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while writing updated product details to file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Example usage:
  /*  public static void main(String[] args) {
        Product product1 = new Product("Apple", "P001", "Fruits", "Large", "Fresh and juicy", 1.5, 100);
        Product product2 = new Product("Banana", "P002", "Fruits", "Medium", "Rich in potassium", 1.0, 150);
        Product product3 = new Product("Milk", "P003", "Dairy", "1 liter", "Full fat", 2.5, 50);

        // Save product details to a file
        product1.saveToFile("products.csv");
        product2.saveToFile("products.csv");
        product3.saveToFile("products.csv");
        // Update product with productId "P002" in products.csv
        Product updatedProduct = new Product("Banana", "P002", "Fruits", "Large", "Fresh and delicious", 1.2, 200);
        updateProduct("products.csv", "P002", updatedProduct);
    }*/
}
