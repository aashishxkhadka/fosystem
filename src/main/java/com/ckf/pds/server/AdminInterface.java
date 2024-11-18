/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ckf.pds.server;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.Product;
import com.ckf.pds.model.User;
import com.ckf.pds.model.UserType;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AdminInterface extends Remote {

    /**
     * Log in as an admin.
     *
     * @param username The admin username.
     * @param password The admin password.
     * @return True if the login is successful, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */

    /**
     * Add a new user to the system.
     *
     * @param username       The username for the new user.
     * @param password       The password for the new user.
     * @param firstName      The first name of the new user.
     * @param lastName       The last name of the new user.
     * @param passportNumber The passport number of the new user.
     * @return True if the user addition is successful, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean addUser(String username, String password, UserType userType, String firstName,
                           String lastName, String passportNumber, String address, String email,
                           String phoneNumber) throws RemoteException;
    
    public String getFirstName(int userId) throws RemoteException;
    public int login(String username, String password) throws RemoteException;
    public List<Product> getAllProducts() throws RemoteException;
    public boolean addProduct(String productName, double productPrice, String productDescription, String category) throws RemoteException;
    public List<Feedback> viewFeedback() throws RemoteException;
    public boolean updateOrderStatus(int orderId, String newStatus) throws RemoteException;
    public List<Order> viewOrders() throws RemoteException;
    public List<User> getAllUsers() throws RemoteException ;
    
    
}


