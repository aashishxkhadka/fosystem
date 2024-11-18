package com.ckf.pds.server;

import com.ckf.pds.Database.DBConnection;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            // Create an instance of the UserServer
            UserServer userServer = new UserServer();

            // Create an instance of the AdminServer
            AdminServer adminServer = new AdminServer();

            // Unexport the existing objects, if any
            UnicastRemoteObject.unexportObject(userServer, true);
            UnicastRemoteObject.unexportObject(adminServer, true);
            
            // Export the server objects to make them available for incoming RMI requests
            UserInterface userStub = (UserInterface) UnicastRemoteObject.exportObject(userServer, 0);
            AdminInterface adminStub = (AdminInterface) UnicastRemoteObject.exportObject(adminServer, 0);

        // Instead of using the default registry port (1099), you can use different ports
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT); // This line is okay for the registry creation

        Naming.rebind("rmi://localhost:1099/userServer", userStub);
        Naming.rebind("rmi://localhost:1099/adminServer", adminStub);


        System.out.println("Server Started");
        
            System.out.println("Server is ready.");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    // Other server-related methods can be added here
    // ...
}
