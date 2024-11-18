package com.ckf.pds.server;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.ckf.pds.Database.DBConnection;
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.Product;
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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminServer extends UnicastRemoteObject implements AdminInterface {

    protected AdminServer() throws RemoteException {
        super();
    }

    // Assuming you have a method to validate admin credentials in your database
    private boolean validateAdminCredentials(String username, String password) throws RemoteException, ClassNotFoundException{
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Admins WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If a row is returned, credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, consider credentials as not valid
        }
    }

    @Override
     public int login(String username, String password) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT user_id, user_type FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Check if the user type is 'admin'
                    if ("Admin".equals(resultSet.getString("user_type"))) {
                        // Login successful for admin, return the user ID
                        return resultSet.getInt("user_id");
                    } else {
                        // User is not an admin, return a different code (-2)
                        return -1;
                    }
                } else {
                    // Login failed, return a different code (-1)
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
            return -1; // Error occurred during class loading
        }
    }

   
     @Override
    public String getFirstName(int userId) throws RemoteException {
        System.out.println("before connection user_id: " + userId);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT first_name FROM users WHERE user_id = ?")) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Return the first name of the user
                    System.out.println("First_name"+ resultSet.getString("first_name"));
                    return resultSet.getString("first_name");
                    
                } else {
                    // User not found
                    System.out.println("User not found with user_id: " + userId);
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during SQL operation: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error during class loading: " + ex.getMessage());
            return null;
        }
    }

     
   @Override
   
    public boolean addUser(String username, String password, UserType userType, String firstName,
                           String lastName, String passportNumber, String address, String email,
                           String phoneNumber) throws RemoteException {
        try {
            // Check if the username is already taken
            if (isUsernameTaken(username)) {
                return false; // Username is not unique, return false
            }

            // Check if the passport number is already taken
            if (isPassportNumberTaken(passportNumber)) {
                return false; // Passport number is not unique, return false
            }

            // Continue with user registration
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO users (username, password, user_type, first_name, last_name, passport_number, address, email, phone_number, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, userType.toString());
                statement.setString(4, firstName);
                statement.setString(5, lastName);
                statement.setString(6, passportNumber);
                statement.setString(7, address);
                statement.setString(8, email);
                statement.setString(9, phoneNumber);

                // Assuming you have a utility method to convert Date to java.sql.Date
                java.sql.Date registrationSqlDate = convertToSqlDate(new Date());
                statement.setDate(10, registrationSqlDate);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0; // If rows were affected, user addition is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean isUsernameTaken(String username) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE username = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // If a row is returned, username is taken
        }
    }

    private boolean isPassportNumberTaken(String passportNumber) throws SQLException,ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE passport_number = ?")) {

            preparedStatement.setString(1, passportNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // If a row is returned, passport number is taken
        }
    }

    // Other methods...

    // Utility method to convert java.util.Date to java.sql.Date
    private java.sql.Date convertToSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public boolean addProduct(String productName, double productPrice, String productDescription, String category) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO products (product_name, product_price, product_description, category) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, productName);
            statement.setDouble(2, productPrice);
            statement.setString(3, productDescription);
            statement.setString(4, category);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // If rows were affected, product addition is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
            
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
//
    public List<User> getAllUsers() throws RemoteException {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("user_type"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("passport_number"),
                        resultSet.getString("address"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getDate("registration_date")
                        
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userList;
    }
//
//    @Override
//    public List<Product> getAllProducts() throws RemoteException {
//    
//        return new ArrayList<>();
//    }
//
//   
    @Override
    public List<Feedback> viewFeedback() throws RemoteException {
        List<Feedback> feedbackList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM feedback")) {

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                Date date = resultSet.getDate("date");

                Feedback feedback = new Feedback(feedbackId, userId, title, description, date);
                feedbackList.add(feedback);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving feedbacks", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return feedbackList;
    }

//
    @Override
    public boolean updateOrderStatus(int orderId, String newStatus) throws RemoteException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Orders SET delivery_status = ? WHERE order_id = ?")) {
            statement.setString(1, newStatus);
            statement.setInt(2, orderId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // If rows were affected, status change is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database operation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public List<Order> viewOrders() throws RemoteException {
        List<Order> orderList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM orders")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getInt("user_id"),
                            resultSet.getDate("order_date"),
                            resultSet.getString("delivery_status"),
                            resultSet.getDate("delivered_date"),
                            resultSet.getDouble("total_amount")
                    );

//                    List<OrderItem> orderItems = getOrderItemsForOrder(order.getOrderId());
//                    order.setItems(orderItems);

                    orderList.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving orders from the database.", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return orderList;
    }


    

  

   
}
