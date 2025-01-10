package com.ecommerce;

import com.ecommerce.utils.DBUtil;
import com.ecommerce.models.Cart;
import com.ecommerce.models.Product;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	private static Customer loggedInCustomer = null;  
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Login or register customer
        System.out.println("Welcome to the E-Commerce System!");
        while (loggedInCustomer == null) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            
            if (option == 1) {
                // Register new customer
                System.out.print("Enter name: ");
                String name = scanner.next();
                System.out.print("Enter email: ");
                String email = scanner.next();
                System.out.print("Enter address: ");
                scanner.nextLine();  // Consume the newline
                String address = scanner.nextLine();
                DBUtil.addCustomer(name, email, address);
                
            } else if (option == 2) {
                // Customer login
                System.out.print("Enter email: ");
                String email = scanner.next();
                loggedInCustomer = DBUtil.getCustomerByEmail(email);
                
                if (loggedInCustomer != null) {
                    System.out.println("Welcome, " + loggedInCustomer.getName() + "!");
                } else {
                    System.out.println("Customer not found. Please register first.");
                }
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        Cart cart = new Cart();  // Cart object to hold the user's cart
        
        // Display menu options
        while (true) {
            System.out.println("\nWelcome to the E-Commerce System!");
            System.out.println("1. Add a new product");
            System.out.println("2. Browse products");
            System.out.println("3. Add product to cart");
            System.out.println("4. Remove product from cart");
            System.out.println("5. Update quantity in cart");
            System.out.println("6. Checkout");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    // Add a new product
                    System.out.println("\n--- Add a New Product ---");
                    System.out.print("Enter product name: ");
                    String name = scanner.next();
                    System.out.print("Enter product category: ");
                    String category = scanner.next();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter product stock quantity: ");
                    int stockQuantity = scanner.nextInt();
                    System.out.print("Enter product description: ");
                    scanner.nextLine();  // Consume the newline character
                    String description = scanner.nextLine();
                    
                    // Add product to database
                    DBUtil.addProduct(name, category, price, stockQuantity, description);
                    break;
                
                case 2:
                    // Browse products
                    System.out.println("\n--- Browse Products ---");
                    DBUtil.fetchProducts();
                    break;
                
                case 3:
                    // Add product to cart
                    System.out.println("\n--- Add Product to Cart ---");
                    System.out.print("Enter product ID to add to cart: ");
                    int productIdToAdd = scanner.nextInt();
                    System.out.print("Enter quantity: ");
                    int quantityToAdd = scanner.nextInt();
                    
                    // Adding product to cart
                    Product productToAdd = getProductById(productIdToAdd);
                    if (productToAdd != null) {
                        CartItem cartItem = new CartItem(productToAdd, quantityToAdd);
                        cart.addItem(cartItem);
                        System.out.println("Product added to cart successfully.");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 4:
                    // Remove product from cart
                    System.out.println("\n--- Remove Product from Cart ---");
                    System.out.print("Enter product ID to remove from cart: ");
                    int productIdToRemove = scanner.nextInt();
                    
                    if (cart.removeItem(productIdToRemove)) {
                        System.out.println("Product removed from cart.");
                    } else {
                        System.out.println("Product not found in cart.");
                    }
                    break;

                case 5:
                    // Update quantity in cart
                    System.out.println("\n--- Update Product Quantity in Cart ---");
                    System.out.print("Enter product ID to update quantity: ");
                    int productIdToUpdate = scanner.nextInt();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    
                    if (cart.updateItemQuantity(productIdToUpdate, newQuantity)) {
                        System.out.println("Quantity updated.");
                    } else {
                        System.out.println("Product not found in cart.");
                    }
                    break;

                case 6:
                    // Checkout
                    System.out.println("\n--- Checkout ---");

                    // Display all items in the cart with their quantities
                    System.out.println("\nYour Cart: ");
                    cart.displayItems();  // Assuming you have a method to display items in the cart
                    
                    // Calculate the total amount
                    double totalAmount = cart.calculateTotal();
                    System.out.println("Total amount: $" + totalAmount);
                    
                    // Ask user if they want to proceed with the checkout
                    System.out.print("Proceed to checkout? (yes/no): ");
                    String checkoutConfirmation = scanner.next();
                    
                    if (checkoutConfirmation.equalsIgnoreCase("yes")) {
                        // Checkout the cart (mark items as bought, reduce stock, etc.)
                        cart.checkout();
                        System.out.println("Checkout successful. Thank you for your purchase!");
                    } else {
                        System.out.println("Checkout cancelled.");
                    }
                    break;


                case 7:
                    // Exit the application
                    System.out.println("Thank you for shopping with us!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Helper method to get a product by ID (simulated for this example)
 // Helper method to get a product by ID from the database
    private static Product getProductById(int productId) {
        String query = "SELECT * FROM Product WHERE product_id = ?";
        
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");
                String description = rs.getString("description");
                
                return new Product(productId, name, category, price, stockQuantity, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no product found
    }

}
