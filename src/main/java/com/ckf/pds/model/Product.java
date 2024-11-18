/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.model;

import java.io.Serializable;

/**
 *
 * @author Sanjok
 */
public class Product implements Serializable {
    private int productId;
    private String productName;
    private double productPrice;
    private String productDescription;
    private String category;

    // Constructors
    public Product() {
    }

    public Product(String productName, double productPrice, String productDescription, String category) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
    }
    public Product(int productId, String productName, double productPrice, String productDescription, String category) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    // Other methods as needed
}

