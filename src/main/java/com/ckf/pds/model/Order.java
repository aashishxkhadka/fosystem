/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.model;

/**
 *
 * @author Sanjok
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    
    private int orderId;
    private int userId;
    private Date orderDate;
    private DeliveryStatus deliveryStatus;
    private Date deliveredDate;
    private Double totalAmount;
    private List<OrderItem> items;

    // Constructors
    public Order() {
    }

    public Order(int userId, DeliveryStatus deliveryStatus) {
        this.userId = userId;
        this.orderDate = new Date();
        this.deliveryStatus = deliveryStatus;
    }
    public Order(int userId, DeliveryStatus deliveryStatus, Date delivered_date,List<OrderItem> items) {
        this.userId = userId;
        this.orderDate = new Date();
        this.deliveryStatus = deliveryStatus;
        this.deliveredDate = delivered_date;
        this.items = items;
        
    }
    public Order(int order_id, int userId,Date order_date, DeliveryStatus deliveryStatus, Date delivered_date, Double totalAmount) {
        this.orderId = order_id;
        this.userId = userId;
        this.orderDate = order_date;
        this.deliveryStatus = deliveryStatus;
        this.deliveredDate = delivered_date;
        this.totalAmount = totalAmount;
          
    }
        public Order(int order_id, int userId,Date order_date, String deliveryStatus, Date delivered_date, Double totalAmount) {
        this.orderId = order_id;
        this.userId = userId;
        this.orderDate = order_date;
        this.deliveryStatus = DeliveryStatus.fromString(deliveryStatus);
        this.deliveredDate = delivered_date;
        this.totalAmount = totalAmount;
          
    }
    public Order(int orderId, int userId, Date orderDate, String deliveryStatus, Date deliveredDate, List<OrderItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.deliveryStatus = DeliveryStatus.fromString(deliveryStatus);
        this.deliveredDate = deliveredDate;
        this.items = items;
        

    }

    
    

   
    

    

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

