package com.ecommerce.models;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String address;

    // Constructor
    public Customer(int customerId, String name, String email, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    // Getters and setters
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
}
