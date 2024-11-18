/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.client;

/**
 *
 * @author Sanjok
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminClientInterface extends Remote {

    // Method to notify the admin about the result of user addition
    void notifyUserAdditionResult(boolean success, String message) throws RemoteException;

    void displayMenu() throws RemoteException;
    public void logOut() throws RemoteException;
    public void login() throws RemoteException;
    public void viewProducts() throws RemoteException;
    public void addProduct() throws RemoteException;
    public void viewFeedback() throws RemoteException;
       public void updateOrderStatus() throws RemoteException;
    
    public void viewOrder() throws RemoteException ;
    public void viewUsers() throws RemoteException;
    
    // Other methods for additional admin-related notifications
    // ...
}

