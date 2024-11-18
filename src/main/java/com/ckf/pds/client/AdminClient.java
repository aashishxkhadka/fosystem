/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.client;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.Product;
import com.ckf.pds.model.User;
import com.ckf.pds.model.UserType;
import com.ckf.pds.server.AdminInterface;
import com.ckf.pds.server.UserInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class AdminClient extends UnicastRemoteObject implements AdminClientInterface {

    private AdminInterface adminServer;
    private UserInterface userServer;

     public AdminClient(AdminInterface adminServer, UserInterface userServer) throws RemoteException {
        this.adminServer = adminServer;
        this.userServer = userServer;
    }
     int loggedInUserId =0;

    
    

    // Method to notify the admin about the result of user addition
    @Override
    public void notifyUserAdditionResult(boolean success, String message) throws RemoteException {
        if (success) {
            System.out.println("User addition successful: " + message);
        } else {
            System.out.println("User addition failed: " + message);
        }
    }
    public void displayMenu()throws RemoteException {
         Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View Products");
            System.out.println("2. View Users");
            System.out.println("3. Add User");
            System.out.println("4. Add Product");
            System.out.println("5. View Feedbacks");
            System.out.println("6. Views Orders");
            System.out.println("7. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            try {
                switch (choice) {
                    case 1:
                        viewProducts();
                        break;

                    case 2:
                        viewUsers();
                        break;
                    case 3:
                        addUser();
                        break;

                    case 4:
                        addProduct();
                        break;

                    case 5:
                        viewFeedback();
                        break;

                    case 6:
                        viewOrder();
                        break;

                    case 7:
                        logOut();
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Error communicating with the server.");
            }
        }
        
    }
    public void viewUsers() throws RemoteException {
        try {
            List<User> userList = adminServer.getAllUsers();

            System.out.println("User List:");
            System.out.println("-----------------------------------------------------------------------------");
            System.out.printf("%-10s | %-15s | %-15s | %-10s | %-15s | %-15s | %-20s | %-20s | %-20s | %-20s%n",
                    "User ID", "Username", "Password", "User Type", "First Name", "Last Name",
                    "Passport Number", "Address", "Email", "Phone Number");
            System.out.println("-----------------------------------------------------------------------------");

            for (User user : userList) {
                System.out.printf("%-10d | %-15s | %-15s | %-10s | %-15s | %-15s | %-20s | %-20s | %-20s | %-20s%n",
                        user.getUserId(), user.getUsername(), user.getPassword(),
                        user.getUserType(), user.getFirstName(), user.getLastName(),
                        user.getPassportNumber(), user.getAddress(), user.getEmail(),
                        user.getPhoneNumber());
            }

            System.out.println("-----------------------------------------------------------------------------");

            // Prompt the user to press any key to return to the User Menu
                System.out.println("Press any key to return to the Admin Menu.");
                new Scanner(System.in).nextLine();
                
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Error retrieving users from the server.");
        }
    }

    @Override
    public void viewProducts() throws RemoteException{
        // Assume productsList is a List<Product> retrieved from the server
        List<Product> productsList = adminServer.getAllProducts();
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) productsList.size() / pageSize);
        int currentPage = 0;
        while (currentPage < totalPages) {
            System.out.println("Product List - Page " + (currentPage+1) + " / " + totalPages);
            System.out.println("----------------------------------------------------");
            System.out.printf("%-5s | %-20s | %-10s | %-30s | %-20s%n",
                    "ID", "Name", "Price", "Description", "Category");
            System.out.println("----------------------------------------------------");

            int startIndex = (currentPage ) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, productsList.size());

            for (int i = startIndex; i < endIndex; i++) {
                Product product = productsList.get(i);
                System.out.printf("%-5d | %-20s | %-10.2f | %-30s | %-20s%n",
                        product.getProductId(), product.getProductName(),
                        product.getProductPrice(), product.getProductDescription(),
                        product.getCategory());
            }

            System.out.println("----------------------------------------------------");
            System.out.println("Options:");
            System.out.println("1. Previous Page");
            System.out.println("2. Next Page");
            System.out.println("3. Go back to Admin Menu");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (currentPage !=0) {
                        currentPage--;
                    } else {
                        System.out.println("Already on the first page.");
                    }
                    break;

                case 2:
                if (currentPage < totalPages-1) {
                    currentPage++;
                } else {
                    System.out.println("Already on the last page.");
                }
                break;
                
                case 3:
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }
    
    
    
    public void addUser() throws RemoteException{
        try {
            Scanner scanner = new Scanner(System.in);

            // Get user input
            System.out.println("User Registration:");
            
            String username;
            boolean isUsernameUnique;
            do {
                System.out.print("Username: ");
                username = scanner.nextLine();
                isUsernameUnique = userServer.isUsernameAvailable(username);
                if (!isUsernameUnique) {
                    System.out.println("Error: Username is not available. Please choose another username.");
                }
            } while (!isUsernameUnique);

            String password;
            String rePassword;
            do {
                System.out.print("Password: ");
                password = scanner.nextLine();

                System.out.print("Re-enter Password: ");
                rePassword = scanner.nextLine();

                if (!password.equals(rePassword)) {
                    System.out.println("Error: Passwords do not match. Please try again.");
                }
            } while (!password.equals(rePassword));

            String firstName = getUserInput("First Name: ");
            String lastName = getUserInput("Last Name: ");
            String userType = "admin";
            
            do{
                 userType = getUserInput("User Type:\n1.Admin\n2.User\nChoose user type: ");
            }while(!(userType.equals("1")|| userType.equals("2")));
            if(userType.equals("1")){
                userType="Admin";
            }else{
                userType = "User";
            }
            
            String passportNumber;
            boolean isPassportNumberUnique;

            do {
                System.out.print("Passport Number: ");
                passportNumber = scanner.nextLine().trim();
                isPassportNumberUnique = userServer.isPassportNumberAvailable(passportNumber);

                if (!isPassportNumberUnique) {
                    System.out.println("Error: Passport number is already associated with an account. Please enter another passport number.");
                    System.out.print("Press any key to go back to the main menu: ");
                    scanner.nextLine();  // Wait for user input
                    return;  // Stop registration and go back to the main menu
                }
            } while (!isPassportNumberUnique);

            String address = getUserInput("Address: ");
            String email = getUserInput("Email: ");
            String phoneNumber = getUserInput("Phone Number: ");

            // Perform user registration
            if(userServer.registerUser(username, password, firstName, lastName,userType, passportNumber, address, email, phoneNumber)){
                System.out.println("User succesfully registered");
                // Prompt the user to press any key to return to the User Menu
                System.out.println("Press any key to return to the Admin Menu.");
                new Scanner(System.in).nextLine();
                displayMenu();
            }else{
                System.out.println("User registeration failed");
            }

        } catch (RemoteException e) {
            System.err.println("RemoteException in UserClient register: " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    
    @Override
    public void viewOrder() throws RemoteException {
        try {
            List<Order> orderList = adminServer.viewOrders();

            System.out.println("Order List:");
            System.out.println("----------------------------------------------------");
            System.out.printf("%-10s | %-10s | %-15s | %-15s | %-20s | %-20s%n",
                    "Order ID", "User ID", "Order Date", "Delivery Status", "Delivered Date", "Total Amount");
            System.out.println("----------------------------------------------------");

            for (Order order : orderList) {
                System.out.printf("%-10d | %-10d | %-15s | %-15s | %-20s | %-20.2f%n",
                        order.getOrderId(), order.getUserId(),
                        order.getOrderDate(), order.getDeliveryStatus(),
                        order.getDeliveredDate(), order.getTotalAmount());
            }

            System.out.println("----------------------------------------------------");

            Scanner scanner = new Scanner(System.in);

            System.out.println("Options:");
            System.out.println("1. Update Order Status");
            System.out.println("2. Go back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    updateOrderStatus();
                    break;

                case 2:
                    // Go back to the admin menu
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 2.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Error retrieving orders from the server.");
        }
    }

    @Override
    public void updateOrderStatus() throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the Order ID to update status: ");
        int orderId = scanner.nextInt();

        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter the new Delivery Status: ");
        String newStatus = scanner.nextLine().trim();

        boolean success = adminServer.updateOrderStatus(orderId, newStatus);

        if (success) {
            System.out.println("Order status updated successfully.");
        } else {
            System.out.println("Error updating order status.");
        }
    }


    @Override
    public void addProduct() throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adding a new product:");

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter product price: ");
        double productPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter product description: ");
        String productDescription = scanner.nextLine();

        System.out.print("Enter product category: ");
        String category = scanner.nextLine();

        try {
            boolean isSuccess = adminServer.addProduct(productName, productPrice, productDescription, category);

            if (isSuccess) {
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Failed to add the product. Please try again.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error communicating with the server.");
        }
    }
    
    @Override
    public void viewFeedback() throws RemoteException {
    try {
        List<Feedback> feedbackList = adminServer.viewFeedback();

        System.out.println("Feedback List:");
        System.out.println("----------------------------------------------------");
        System.out.printf("%-5s | %-10s | %-20s | %-50s | %-15s%n",
                "ID", "User ID", "Title", "Description", "Date");
        System.out.println("----------------------------------------------------");

        for (Feedback feedback : feedbackList) {
            System.out.printf("%-5d | %-10d | %-20s | %-50s | %-15s%n",
                    feedback.getId(), feedback.getUserId(),
                    feedback.getTitle(), feedback.getDescription(),
                    feedback.getDate());
        }

        System.out.println("----------------------------------------------------");

    } catch (RemoteException e) {
        e.printStackTrace();
        System.err.println("Error retrieving feedbacks from the server.");
    }
}

    
    @Override
    public void login() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        int maxAttempts = 3;
        int attempts = 0;

        try {
            do {
                System.out.println("Enter your username:");
                String username = scanner.nextLine();

                System.out.println("Enter your password:");
                String password = scanner.nextLine();

                // Assume userServer is the reference to the UserServer remote object
                int loggedInUserId = adminServer.login(username, password);
                

                if (loggedInUserId > 0) {
                    String firstName = adminServer.getFirstName(loggedInUserId);
                    System.out.println("Login successful! Welcome, " + firstName + ".");
                    this.loggedInUserId = loggedInUserId;
                    displayMenu();
                    return; // Exit the method after successful login
                } else {
                    System.out.println("Login failed. Please check your username and password.");
                    attempts++;
                    if (attempts < maxAttempts) {
                        System.out.println("You have " + (maxAttempts - attempts) + " attempts remaining.");
                    } else {
                        System.out.println("Maximum login attempts reached. Returning to the main menu.");
                        return; // Exit the method after reaching maximum login attempts
                    }
                }
            } while (attempts < maxAttempts);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void logOut() throws RemoteException {
        System.out.println("Logging out. Goodbye!");
        System.exit(0); // Terminate the application
    }
    private String getUserInput(String prompt) throws RemoteException{
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            return scanner.nextLine();
        }
    // Othe

    // Other admin-related methods as needed
}
