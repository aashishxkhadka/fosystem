/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.server;

/**
 *
 * @author Sanjok
 */

import com.ckf.pds.Database.DBConnection;
import com.ckf.pds.model.CartItem;
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.OrderItem;
import com.ckf.pds.model.Product;
import com.ckf.pds.model.ShoppingCart;
import com.ckf.pds.model.User;
import com.ckf.pds.model.UserType;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServer extends UnicastRemoteObject implements UserInterface {



    private Map<String, User> users;

    public UserServer() throws RemoteException {
        users = new HashMap<>();
    }


        @Override
    public boolean isUsernameAvailable(String username) throws RemoteException {
          try (Connection connection = DBConnection.getConnection();
               PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE username = ?")) {

              preparedStatement.setString(1, username);

              try (ResultSet resultSet = preparedStatement.executeQuery()) {
                  return !resultSet.next();
              }

          } catch (SQLException | ClassNotFoundException e) {
              String errorMessage = "Error checking username availability. SQL query: SELECT * FROM Users WHERE username = ?";
              throw new RemoteException(errorMessage, e);
          }
      }
       
       


    @Override
    public boolean isPassportNumberAvailable(String passportNumber) throws RemoteException {
    try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE TRIM(passport_number) = ?")) {

        preparedStatement.setString(1, passportNumber.trim());

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return !resultSet.next();
        }
    } catch (SQLException | ClassNotFoundException e) {
        throw new RemoteException("Error checking passport number availability", e);
    }
}


    public boolean registerUser(String username, String password, String firstName, String lastName,String usertype,
                                String passportNumber, String address, String email, String phoneNumber) throws RemoteException {
        
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Users (username, password, user_type, first_name, last_name, passport_number, " +
                             "address, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, usertype);
            preparedStatement.setString(4, firstName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setString(6, passportNumber);
            preparedStatement.setString(7, address);
            preparedStatement.setString(8, email);
            preparedStatement.setString(9, phoneNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RemoteException("Error registering user", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }
    
    @Override
    public int login(String username, String password) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT user_id FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Login successful, return the user ID
                    return resultSet.getInt("user_id");
                } else {
                    // Login failed
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    @Override
    public String getFirstName(int userId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT first_name FROM users WHERE user_id = ?")) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Return the first name of the user
                    return resultSet.getString("first_name");
                } else {
                    // User not found
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



    
    @Override
    public List<Product> getAllProducts() throws RemoteException {
        List<Product> productList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("product_price"),
                        resultSet.getString("product_description"),
                        resultSet.getString("category")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productList;
    }

    
    
    @Override
    public List<Order> getMyOrders(int userId) throws RemoteException {
        List<Order> myOrders = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM orders WHERE user_id = ?")) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("order_id"),
                            userId,
                            resultSet.getDate("order_date"),
                            resultSet.getString("delivery_status"),
                            resultSet.getDate("delivered_date"),
                            new ArrayList<>() // This list will be populated in the next step
                    );
                    

                    // Now, fetch order items for each order
                    List<OrderItem> orderItems = getOrderItemsForOrder(order.getOrderId());
                    order.setItems(orderItems);

                    myOrders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return myOrders;
    }

    private List<OrderItem> getOrderItemsForOrder(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM orderitems WHERE order_id = ?")) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderItem orderItem = new OrderItem(
                            resultSet.getInt("order_item_id"),
                            orderId,
                            resultSet.getInt("product_id"),
                            resultSet.getInt("quantity")
                    );

                    
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orderItems;
    }
    
    
     
    public Product getProductById(int productId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE product_id = ?")) {
            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Product(
                            resultSet.getInt("product_id"),
                            resultSet.getString("product_name"),
                            resultSet.getDouble("product_price"),
                            resultSet.getString("product_description"),
                            resultSet.getString("category")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Return null if product with the given ID is not found
    }
    
    public List<ShoppingCart> getShoppingCarts(int userId) throws RemoteException {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM shoppingcarts WHERE user_id = ?")) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ShoppingCart shoppingCart = new ShoppingCart(
                            resultSet.getInt("id"),
                            userId,
                            new ArrayList<>() // This list will be populated in the next step
                    );

                    // Now, fetch cart items for each cart
                    List<CartItem> cartItems = getCartItemsForCart(shoppingCart.getUserId());
                    shoppingCart.setCartItems(cartItems);

                    shoppingCarts.add(shoppingCart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return shoppingCarts;
    }
    
    
    
    
    public List<CartItem> getCartItemsForCart(int user_id) throws RemoteException {
    List<CartItem> cartItems = new ArrayList<>();

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(
                 "SELECT id FROM shoppingcarts WHERE user_id = ?");
         PreparedStatement statement1 = connection.prepareStatement(
                 "SELECT id, product_id, quantity,rate FROM cartitem WHERE cart_id = ?")) {
        statement.setInt(1, user_id);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int cartId = resultSet.getInt("id");

                statement1.setInt(1, cartId);
                try (ResultSet resultSet1 = statement1.executeQuery()) {
                    while (resultSet1.next()) {
                        int id = resultSet1.getInt("id");
                        int productId = resultSet1.getInt("product_id");
                        int quantity = resultSet1.getInt("quantity");
                        int rate = resultSet1.getInt("rate");

                        CartItem cartItem = new CartItem(id,cartId,productId, quantity,rate);
                        cartItems.add(cartItem);
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception appropriately
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
    }

    return cartItems;
}

    
    @Override
    public void addProductToCart(int cartId, int productId, int quantity, int rate) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO cartitem (cart_id, product_id, quantity, rate) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, cartId);
            statement.setInt(2, productId);  // Assuming loggedInUserId is a variable in your class
            statement.setInt(3, quantity);
            statement.setInt(4, rate);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product added to the shopping cart successfully.");
            } else {
                System.out.println("Failed to add product to the shopping cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ShoppingCart createShoppingCart(int userId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Shoppingcarts (user_id) VALUES (?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating shopping cart failed. No rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int cartId = generatedKeys.getInt(1);
                    return new ShoppingCart(cartId, userId);
                } else {
                    throw new SQLException("Creating shopping cart failed. No ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database error while creating shopping cart.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     @Override
    public int getRateOfProduct(int productId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT product_price FROM products WHERE product_id = ?")) {
            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("product_price");
                } else {
                    // Handle the case when the product with the given ID is not found
                    throw new IllegalArgumentException("Product with ID " + productId + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
            throw new RemoteException("Error retrieving product rate.", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void removeProductFromCart(int cartId, int productId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM cartitem WHERE cart_id = ? AND product_id = ?")) {
            statement.setInt(1, cartId);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Product not found in the cart.");
            } else {
                System.out.println("Product removed from the cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error removing product from cart.", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void deleteShoppingCart(int cartId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement deleteCartItemsStatement = connection.prepareStatement(
                     "DELETE FROM cartitem WHERE cart_id = ?");
             PreparedStatement deleteShoppingCartStatement = connection.prepareStatement(
                     "DELETE FROM shoppingcarts WHERE id = ?")) {

            // Delete cart items first
            deleteCartItemsStatement.setInt(1, cartId);
            int cartItemsRowsAffected = deleteCartItemsStatement.executeUpdate();

            // Now, delete the shopping cart
            deleteShoppingCartStatement.setInt(1, cartId);
            int shoppingCartRowsAffected = deleteShoppingCartStatement.executeUpdate();

            if (cartItemsRowsAffected == 0 && shoppingCartRowsAffected == 0) {
                System.out.println("Shopping cart not found.");
            } else {
                System.out.println("Shopping cart and associated items deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error deleting shopping cart.", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public boolean checkout(int userId, int cartId) throws RemoteException {
        try (Connection connection = DBConnection.getConnection()) {
            // Retrieve cart items
            List<CartItem> cartItems = getCartItemsForCart(userId);

            // Calculate total amount
            double totalAmount = calculateTotalAmount(cartItems);

            // Create a new order
            int orderId = addOrder(userId, totalAmount);

            // Add order items
            addOrderItems(orderId, cartItems);

            // Update delivery status or perform other order-related tasks

            // Delete cart and cart items
            deleteShoppingCart(cartId);

            return true; // Return true if checkout is successful
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error during checkout.", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
            return false; // Return false if an exception occurs
        }
    }
    // Additional helper methods (customize these based on your database operations)

    public double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0;
        for (CartItem cartItem : cartItems) {
            totalAmount += cartItem.getQuantity() * cartItem.getRate();
        }
        return totalAmount;
    }
    private int addOrder(int userId, double totalAmount) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO orders (user_id, order_date, total_amount, delivery_status) VALUES (?, NOW(), ?, 'Pending')",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setDouble(2, totalAmount);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private void addOrderItems(int orderId, List<CartItem> cartItems) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO orderitems (order_id, product_id, quantity, rate) VALUES (?, ?, ?, ?)")) {
            for (CartItem cartItem : cartItems) {
                statement.setInt(1, orderId);
                statement.setInt(2, cartItem.getProductId());
                statement.setInt(3, cartItem.getQuantity());
                statement.setInt(4, cartItem.getRate());

                statement.addBatch();
            }

            statement.executeBatch();
        }   catch (ClassNotFoundException ex) {
                Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public boolean addFeedback(Feedback feedback) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO feedback (user_id, title, description, date) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, feedback.getUserId());
            statement.setString(2, feedback.getTitle());
            statement.setString(3, feedback.getDescription());
            statement.setDate(4, new java.sql.Date(feedback.getDate().getTime()));

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RemoteException("Error adding feedback.", e);
        }
    }
    public boolean changePassword(String password, int user_id) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE users SET password = ? WHERE user_id = ?")) {
            statement.setString(1, password);
            statement.setInt(2, user_id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RemoteException("Error changing password.", e);
        }
    }



}



    

    




