package com.ashita.ecommerce.model;

public class Product {

    private String Description, imageUrl, name, price, shippingPrice;
    String userEmail;
    String id;
    String quantity;

    //Alt+insert is shortcut for having getters and setters and constructor.
    public Product() {

    }

    public Product(String userEmail, String id, String name, String price, String shippingPrice, String imageUrl, String quantity) {
        this.userEmail = userEmail;
        this.id = id;
        this.name = name;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Product(String id, String name, String price, String shippingPrice, String imageUrl, String quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Alt+insert to enter getter and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }
}
