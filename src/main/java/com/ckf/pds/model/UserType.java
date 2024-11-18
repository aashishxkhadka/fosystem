/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.model;

/**
 *
 * @author Sanjok
 */
public enum UserType {
    ADMIN("Admin"),
    USER("User");
    private final String typeString;

    UserType(String typeString) {
        this.typeString = typeString;
    }
    @Override
    public String toString() {
        return name(); // Assuming you want the name of the enum as the string representation
    }

    public static UserType fromString(String typeString) {
        for (UserType type : UserType.values()) {
            if (type.typeString.equalsIgnoreCase(typeString.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DeliveryStatus: " + typeString);
    }
}

