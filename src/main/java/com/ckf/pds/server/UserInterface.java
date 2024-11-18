/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ckf.pds.server;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.model.CartItem;
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.Product;
import com.ckf.pds.model.ShoppingCart;
import com.ckf.pds.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface UserInterface extends Remote {

    boolean isUsernameAvailable(String username) throws RemoteException;

    boolean isPassportNumberAvailable(String passportNumber) throws RemoteException;

    public boolean registerUser(String username, String password, String firstName, String lastName,String usertype,
                                String passportNumber, String address, String email, String phoneNumber) throws RemoteException;
    
    public int login(String username, String password) throws RemoteException;
    public String getFirstName(int userId) throws RemoteException;
    public List<Product> getAllProducts() throws RemoteException;
    public List<ShoppingCart> getShoppingCarts(int userId) throws RemoteException;
    public List<CartItem> getCartItemsForCart(int cartId) throws RemoteException;
    public void addProductToCart(int cartId, int productId, int quantity, int loggedInUserId) throws RemoteException ;
    public boolean checkout(int user_id, int cart_id) throws RemoteException;
    public List<Order> getMyOrders(int userId) throws RemoteException;
    public Product getProductById(int productId) throws RemoteException;
    public ShoppingCart createShoppingCart(int userId) throws RemoteException ;
    int getRateOfProduct(int productId) throws RemoteException;
    public void removeProductFromCart(int cartId, int productId) throws RemoteException;
    public void deleteShoppingCart(int cartId) throws RemoteException;
    public boolean addFeedback(Feedback feedback) throws RemoteException;
    public boolean changePassword(String password, int user_id) throws RemoteException;
   
    
    

//    User loginUser(String username, String password) throws RemoteException;

    // Other user-related methods as needed
}


