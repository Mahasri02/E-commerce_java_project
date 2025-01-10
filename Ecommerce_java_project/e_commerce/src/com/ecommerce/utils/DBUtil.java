package com.ecommerce.utils;

import java.sql.*;

import com.ecommerce.models.Customer;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Mahasri@02";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to fetch and display product details
    public static void fetchProducts() {
        String query = "SELECT * FROM Product";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");
                String description = rs.getString("description");
                
                System.out.println("Product ID: " + productId + ", Name: " + name + ", Price: $" + price + ", Stock: " + stockQuantity + ", Description: " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to add a product to the database
    public static void addProduct(String name, String category, double price, int stockQuantity, String description) {
        String query = "INSERT INTO Product (name, category, price, stock_quantity, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setDouble(3, price);
            stmt.setInt(4, stockQuantity);
            stmt.setString(5, description);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    // Method to add a new customer to the database
    public static void addCustomer(String name, String email, String address) {
        String query = "INSERT INTO Customers (name, email, address) VALUES (?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, address);
            stmt.executeUpdate();
            System.out.println("Customer registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a customer by email
    public static Customer getCustomerByEmail(String email) {
        String query = "SELECT * FROM Customers WHERE email = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                return new Customer(customerId, name, email, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Main method for testing
    public static void main(String[] args) {
        // Test the methods
        System.out.println("Fetching products...");
        fetchProducts();  // Fetch and display products
    }
}
