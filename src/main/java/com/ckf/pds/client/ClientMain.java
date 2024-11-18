/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.client;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.server.AdminInterface;
import com.ckf.pds.server.UserInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {
        try {
            // Locate the RMI registry on the server
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

                    // Naming.rebind("rmi://localhost:1099/userServer", userStub);
        // Naming.rebind("rmi://localhost:1099/adminServer", adminStub);

        // Use consistent names when looking up from clients
        UserInterface userServer = (UserInterface) Naming.lookup("rmi://localhost:1099/userServer");
        AdminInterface adminServer = (AdminInterface) Naming.lookup("rmi://localhost:1099/adminServer");


            // Create a UserClient instance for user interaction
            UserClient userClient = new UserClient(userServer);

            // Create an AdminClient instance for admin interaction
            AdminClient adminClient = new AdminClient(adminServer,userServer);


            // Display the menu
            displayMenu(userClient, adminClient);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
     public static void displayMenu(UserClientInterface userClient, AdminClientInterface adminClient) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("PDS-RMI-CLI-Project Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. User Register");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    adminClient.login();
                    break;

                case 2:
                    userClient.login();
                    break;

                case 3:
                    userClient.register();
                    break;

                case 4:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        } while (choice != 4);
    }
}
