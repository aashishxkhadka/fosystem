/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.client;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.model.ShoppingCart;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserClientInterface extends Remote {

    // Method to notify the user about the registration result
    void notifyRegistrationResult(boolean success, String message) throws RemoteException;

    void displayMenu() throws RemoteException;
    
    public void register()throws RemoteException;
    public void login()throws RemoteException;
    void viewProducts() throws RemoteException;

    void viewCart() throws RemoteException;
    public void addProductToShoppingCart() throws RemoteException;
    public ShoppingCart getOrCreateShoppingCart() throws RemoteException;
    public void checkout(int user_id, int cart_id) throws RemoteException;
    void viewMyOrders() throws RemoteException;
    
    void giveFeedback() throws RemoteException;

    public void changePassword(int userId) throws RemoteException;
    
    void logOut() throws RemoteException;
    // Other methods for additional user-related notifications
    // ...
}

