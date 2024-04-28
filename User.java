import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class User {
    private String name;
    private String userId;
    private String password;

    public User(String name, String userId, String password) {
        this.name = name;
        this.userId = userId;
        this.password = password;
    }

    // Getters and Setters for User attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Method to save user details to a CSV file
    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String userLine = String.format("%s,%s,%s\n", name, userId, password);
            writer.write(userLine);
            JOptionPane.showMessageDialog(null, "User details saved to file: " + filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while saving user details to file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update an existing user in the CSV file
    public static void updateUser(String filename, String userId, User updatedUser) {
        List<User> users = new ArrayList<>();

        // Read existing users from the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String existingUserId = parts[1];
                    String password = parts[2];

                    // Create User object from CSV data
                    User user = new User(name, existingUserId, password);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while reading user details from file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the user with the specified userId
        boolean found = false;
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                // Update user attributes
                user.setName(updatedUser.getName());
                user.setPassword(updatedUser.getPassword());
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "User with userId '" + userId + "' not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Write updated users back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                String userLine = String.format("%s,%s,%s\n", user.getName(), user.getUserId(), user.getPassword());
                writer.write(userLine);
            }
            JOptionPane.showMessageDialog(null, "User with userId '" + userId + "' updated successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while writing updated user details to file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Example usage:
    
    public static void main(String[] args) {
        // Update user with userId "admin123" in users.csv
        User updatedUser = new User("Yash", "admin1234", "newpassword");
        updatedUser.saveToFile("users.csv");
        updatedUser.setName("Yash Parmar");
        updateUser("users.csv", "admin1234", updatedUser);

    }
    
}