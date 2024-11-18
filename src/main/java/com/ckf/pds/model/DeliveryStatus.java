/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.model;

/**
 *
 * @author Sanjok
 */
public enum DeliveryStatus  {
    PENDING("Pending"),
    SHIPPED("Shipped"),
    ON_THE_WAY("on_the_way"),
    DELIVERED("Delivered");
    
    private final String statusString;

    DeliveryStatus(String statusString) {
        this.statusString = statusString;
    }

    public static DeliveryStatus fromString(String statusString) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if (status.statusString.equalsIgnoreCase(statusString.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid DeliveryStatus: " + statusString);
    }
    @Override
    public String toString() {
        return name(); // Assuming you want the name of the enum as the string representation
    }
}

