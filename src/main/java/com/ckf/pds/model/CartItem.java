package com.ckf.pds.model;

import java.io.Serializable;

public class CartItem implements Serializable {

   

    

    private int id;
    private int cart_id;
    private int productId;
    private int quantity;
    private int rate;

    // Constructors
    public CartItem() {
    }

    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public CartItem(int id, int cart_id,int productId, int quantity,int rate) {
        this.id = id;
        this.cart_id = cart_id;
        this.productId = productId;
        this.quantity = quantity;
        this.rate = rate;
    }
    
    public CartItem(int productId, int quantity,int rate) {
    this.productId = productId;
    this.quantity = quantity;
    this.rate = rate;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    // Other methods as needed
}
