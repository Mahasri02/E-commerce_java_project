package com.ecommerce.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.utils.DBUtil;

public class Cart {

    private List<CartItem> cartItems;

    // Constructor
    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    // Method to display items in the cart
    public void displayItems() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Items in your cart:");
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            double totalPrice = product.getPrice() * quantity;
            System.out.println("Product: " + product.getName() + ", Category: " + product.getCategory() +
                               ", Price: $" + product.getPrice() + ", Quantity: " + quantity + 
                               ", Total: $" + totalPrice);
        }
    }
    // Add item to cart
    public void addItem(CartItem item) {
        cartItems.add(item);
    }

    // Remove item from cart by product ID
    public boolean removeItem(int productId) {
        return cartItems.removeIf(item -> item.getProduct().getProductId() == productId);
    }

    // Update item quantity in the cart
    public boolean updateItemQuantity(int productId, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId() == productId) {
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    // Calculate total price of all items in the cart
    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // Checkout (clear the cart after checkout)
 // Cart.java (Add to Cart class)

    public void checkout() {
        // Loop through each item in the cart
        for (CartItem item : cartItems) {
            int productId = item.getProduct().getProductId();
            int quantityPurchased = item.getQuantity();
            
            // Check if stock is sufficient in the database
            if (getProductStockQuantity(productId) >= quantityPurchased) {
                // Reduce stock in the database for each item
                String query = "UPDATE Product SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
                
                try (Connection connection = DBUtil.getConnection();
                     PreparedStatement stmt = connection.prepareStatement(query)) {
                    
                    stmt.setInt(1, quantityPurchased);  // Set the quantity purchased to reduce stock
                    stmt.setInt(2, productId);  // Set the product ID
                    int rowsUpdated = stmt.executeUpdate();
                    
                    if (rowsUpdated > 0) {
                        System.out.println("Stock updated for product ID: " + productId);
                    }
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error updating stock for product ID: " + productId);
                }
            } else {
                System.out.println("Insufficient stock for product ID: " + productId);
            }
        }
        
        // Clear the cart after checkout
        cartItems.clear();
        System.out.println("Thank you for your purchase!");
    }

    // Helper method to get the current stock quantity of a product from the database
    private int getProductStockQuantity(int productId) {
        String query = "SELECT stock_quantity FROM Product WHERE product_id = ?";
        int stockQuantity = 0;
        
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                stockQuantity = rs.getInt("stock_quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stockQuantity;
    }


}
