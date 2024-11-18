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
import java.util.Date;

public class User implements Serializable{


    private int userId;
    private String username;
    private String password;
    private UserType userType;
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private Date registrationDate;

    // Constructors
    public User() {
    }

    public User(String username, String password, UserType userType, String firstName, String lastName,
                String passportNumber, String address, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = new Date();
    }
    public User(int userId, String username, String password, String userType, String firstName, String lastName,
                String passportNumber, String address, String email, String phoneNumber, Date registrationDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = UserType.fromString(userType);
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Other methods as needed
}

