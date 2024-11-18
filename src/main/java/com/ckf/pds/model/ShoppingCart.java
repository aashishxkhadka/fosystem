package com.ckf.pds.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable {

    private int cartId;
    private int userId;
    private List<CartItem> cartItems;

    // Constructors
    public ShoppingCart() {
        this.cartItems = new ArrayList<>();
    }

    public ShoppingCart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItems = new ArrayList<>();
    }
    public ShoppingCart(int cartId, int userId, List<CartItem> cartItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItems = cartItems;
    }
    

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Other methods as needed
}
