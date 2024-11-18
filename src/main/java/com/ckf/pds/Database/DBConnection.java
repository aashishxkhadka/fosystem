package com.ckf.pds.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pds";
            String username = "someone";
            String password = "password";

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            // Handle or log the exception as needed
            e.printStackTrace();
            throw e; // Optionally, you can rethrow the exception after handling
        }
    }
    
}
