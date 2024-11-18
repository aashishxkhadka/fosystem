/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ckf.pds.client;

/**
 *
 * @author Sanjok
 */
import com.ckf.pds.model.CartItem;
import com.ckf.pds.model.Feedback;
import com.ckf.pds.model.Order;
import com.ckf.pds.model.OrderItem;
import com.ckf.pds.model.Product;
import com.ckf.pds.model.ShoppingCart;
import com.ckf.pds.server.UserInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserClient extends UnicastRemoteObject implements UserClientInterface {

    private UserInterface userServer;
    private String loggedInUserId;

    public UserClient(UserInterface userServer) throws RemoteException {
        this.userServer = userServer;
        
    }

    

@Override
    public void displayMenu() throws RemoteException {
        
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("User Menu:");
            System.out.println("1. View Products");
            System.out.println("2. View Cart");
            System.out.println("3. View My Orders(Transaction History)");
            System.out.println("4. Give Feedback");
            System.out.println("5. Change Password");
            System.out.println("6. Log Out");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewProducts();
                    break;

                case 2:
                    viewCart();
                    break;

                case 3:
                    viewMyOrders();
                    break;

                
                case 4: 
                        giveFeedback();
                    break;

                case 5:
                    changePassword(Integer.parseInt(loggedInUserId));
                    break;

                case 6:
                    logOut();
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        } while (choice != 6);
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
                int loggedInUserId = userServer.login(username, password);
                

                if (loggedInUserId != -1) {
                    String firstName = userServer.getFirstName(loggedInUserId);
                    System.out.println("Login successful! Welcome, " + firstName + ".");
                    // Store the user ID in the UserClient instance for future use
                    this.loggedInUserId = Integer.toString(loggedInUserId);
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
    public void viewProducts() throws RemoteException {
        // Assume productsList is a List<Product> retrieved from the server
        List<Product> productsList = userServer.getAllProducts();
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
            System.out.println("3. Add Product to Cart");
            System.out.println("4. Go back to User Menu");
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
                    addProductToShoppingCart();
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }


    // Helper method to add a product to the shopping cart
    public void addProductToShoppingCart() throws RemoteException {
        // Get the user's shopping cart or create a new one
        ShoppingCart shoppingCart = getOrCreateShoppingCart();

        // Ask the user to enter the product ID
        System.out.print("Enter the ID of the product you want to add to the cart: ");
        Scanner scanner = new Scanner(System.in);
        int productId = scanner.nextInt();
        System.out.print("Enter the quantity of the product: ");
        int quantity = scanner.nextInt();

        int rate = userServer.getRateOfProduct(productId);
        // Add the product to the shopping cart
        userServer.addProductToCart(shoppingCart.getCartId(), productId, quantity,rate); // Assuming a quantity of 1
        System.out.println("Product added to the shopping cart successfully!");
        // Prompt the user to press any key to return to the User Menu
        System.out.println("Press any key to return to the User Menu.");
        new Scanner(System.in).nextLine();
    }
    
    

    // Helper method to get or create a shopping cart for the user
    public ShoppingCart getOrCreateShoppingCart() throws RemoteException {
        List<ShoppingCart> shoppingCarts = userServer.getShoppingCarts(Integer.parseInt(loggedInUserId));

        if (shoppingCarts.isEmpty()) {
            // If the user has no shopping carts, create a new one
            ShoppingCart newShoppingCart = userServer.createShoppingCart(Integer.parseInt(loggedInUserId));
            System.out.println("A new shopping cart has been created for you.");
            return newShoppingCart;
        } else {
//            // Let the user choose from existing shopping carts
//            System.out.println("Select your shopping cart:");
//            for (int i = 0; i < shoppingCarts.size(); i++) {
//                System.out.println((i + 1) + ". Shopping Cart " + shoppingCarts.get(i).getCartId());
//            }
//
//            System.out.print("Enter the number of your choice: ");
//            Scanner scanner = new Scanner(System.in);
//            int cartChoice = scanner.nextInt();

            // Assuming cartChoice is valid (within the range)
            return shoppingCarts.get(0);
        }
    }


     @Override
    public void viewCart() throws RemoteException {
        try {
            

            // Get cart items for the current shopping cart
            List<CartItem> cartItems = userServer.getCartItemsForCart(Integer.parseInt(loggedInUserId));

            if (cartItems.isEmpty()) {
                System.out.println("Your shopping cart is empty.");
                // Prompt the user to press any key to return to the User Menu
                System.out.println("Press any key to return to the User Menu.");
                new Scanner(System.in).nextLine();
            } else {
                System.out.println("Viewing Cart:");
                System.out.println("----------------------------------------------------");
                System.out.printf("%-5s | %-5s | %-20s | %-10s | %-10s | %-10s%n", "Cart Id","Product Id", "Name", "Quantity", "Rate", "Total Amount");
                System.out.println("----------------------------------------------------");

                for (CartItem cartItem : cartItems) {
                    Product product = userServer.getProductById(cartItem.getProductId());
                    System.out.printf("%-5d | %-5d | %-20s | %-10d | %-10d | %-10d%n",
                            cartItem.getCart_id(),
                            product.getProductId(), product.getProductName(),
                            cartItem.getQuantity(), cartItem.getRate(),(cartItem.getQuantity()*cartItem.getRate()) );
                }

                System.out.println("----------------------------------------------------");
                

                // Display total amount or other information as needed
                // ...

                System.out.println("Options:");
                System.out.println("1. Remove Item from Cart");
                System.out.println("2. Delete Cart");
                System.out.println("3. Checkout");
                System.out.println("4. Go back to User Menu");
                System.out.print("Enter your choice: ");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
//                /ShoppingCart  shoppingCart= ShopppingCart(userServer.getShoppingCarts(loggedInUserId)) ;

                switch (choice) {
                    case 1:
                        System.out.print("Enter the product ID to remove from the cart: ");
                        int productIdToRemove = scanner.nextInt();
                        userServer.removeProductFromCart(cartItems.get(0).getCart_id(), productIdToRemove);
                        System.out.println("Product removed from the cart.");
                        // Prompt the user to press any key to return to the User Menu
                        System.out.println("Press any key to return to the User Menu.");
                        new Scanner(System.in).nextLine();
                        break;

                    case 2:
                        userServer.deleteShoppingCart(cartItems.get(0).getCart_id());
                        System.out.println("Your shopping cart has been deleted.");
                        // Prompt the user to press any key to return to the User Menu
                        System.out.println("Press any key to return to the User Menu.");
                        new Scanner(System.in).nextLine();
                        return;

                    case 3:
                        checkout(Integer.parseInt(loggedInUserId), cartItems.get(0).getCart_id());
                        return;
                    case 4:
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log or handle the exception as needed
            throw new RemoteException("Error viewing cart.", e);
        }
    }
    

    @Override
    public void checkout(int user_id, int cart_id) throws RemoteException {
        System.out.println("Are you sure you want to checkout? (yes/no)");

        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            // Implement logic for checkout
            System.out.println("Checking out..."); // Add your checkout logic here
            if(userServer.checkout(user_id,cart_id)){
                System.out.println("Checkout Succesful!!!");
                viewMyOrders();
                
            }else{
                System.out.println("Checkout failed!!!");
            }
        } else if (response.equals("no")) {
            System.out.println("Returning to User Menu...");
        } else {
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }
    }

    @Override
    public void viewMyOrders() throws RemoteException {
        if (loggedInUserId == null) {
            System.out.println("You are not logged in. Please log in first.");
            return;
        }

        List<Order> myOrders = userServer.getMyOrders(Integer.parseInt(loggedInUserId)); // Assuming Order class is defined
        if (myOrders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("My Orders:");

            // Display table header
            System.out.format("%-10s%-20s%-20s%-15s%-30s%-15s%-15s%n", "Order ID", "Order Date", "Status", "Delivered Date", "Product", "Quantity", "Total Amount");

            // Display each order in tabular format
            for (Order order : myOrders) {
                double totalAmount = 0.0;

                // Display each item in the order
                for (OrderItem item : order.getItems()) {
                    // You need to fetch the actual Product details from the database using item.getProductId()
                    Product product = userServer.getProductById(item.getProductId());

                    int quantity = item.getQuantity();
                    double itemAmount = product.getProductPrice() * quantity;
                    
                    

                    System.out.format(
                            "%-10s%-20s%-20s%-15s%-30s%-15s%-15s%n",
                            order.getOrderId(),
                            order.getOrderDate(),
                            order.getDeliveryStatus(),
                            order.getDeliveredDate(),
                            product.getProductName(),
                            quantity,
                            itemAmount
                    );

                    totalAmount += itemAmount;
                }

                
            }
        }

        // Prompt the user to press any key to return to the User Menu
        System.out.println("Press any key to return to the User Menu.");
        new Scanner(System.in).nextLine();
    }

    
    public void giveFeedback() throws RemoteException {
        try {
            // Get user input for feedback
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter feedback title: ");
            String title = scanner.nextLine();

            System.out.print("Enter feedback description: ");
            String description = scanner.nextLine();

            // Assume the feedback date is the current date
            Date date = new Date();

            // Get logged-in user's ID
            int userId = Integer.parseInt(loggedInUserId);

            // Create a Feedback object
            Feedback feedback = new Feedback(userId, title, description, date);

            // Call the server to add the feedback
            boolean feedbackAdded = userServer.addFeedback(feedback);

            if (feedbackAdded) {
                System.out.println("Feedback submitted successfully!");
                // Prompt the user to press any key to return to the User Menu
                System.out.println("Press any key to return to the User Menu.");
                new Scanner(System.in).nextLine();
            } else {
                System.out.println("Failed to submit feedback. Please try again.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Error giving feedback: " + e.getMessage());
        }
    }


     public void changePassword(int userId) throws RemoteException {

        // Prompt user for a new password and retype
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your new password: ");
        String newPass = scanner.nextLine();

        System.out.print("Retype your new password: ");
        String retypePass = scanner.nextLine();

        // Validate if the new password and retyped password match
        if (!newPass.equals(retypePass)) {
            System.out.println("Passwords do not match. Please try again.");
            return ;
        }
        
        if( userServer.changePassword(newPass,userId)){
            System.out.println("Password changed successfully!");
            // Prompt the user to press any key to return to the User Menu
            System.out.println("Press any key to return to the User Menu.");
            new Scanner(System.in).nextLine();
        }else{
            
            System.out.println("Error changing password.");
        }

        
    }

    @Override
    public void logOut() throws RemoteException {
        System.out.println("Logging out. Goodbye!");
        System.exit(0); // Terminate the application
    }


    // Method to notify the user about the registration result
    @Override
    public void notifyRegistrationResult(boolean success, String message) throws RemoteException {
        if (success) {
            System.out.println("Registration successful: " + message);
        } else {
            System.out.println("Registration failed: " + message);
        }
    }

    
    @Override
        public void register()throws RemoteException {
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
            if(userServer.registerUser(username, password, firstName, lastName,"User", passportNumber, address, email, phoneNumber)){
                System.out.println("User succesfully registered");
                this.loggedInUserId = Integer.toString(userServer.login(username, password));
                // Prompt the user to press any key to return to the User Menu
                System.out.println("Press any key to return to the User Menu.");
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

      private String getUserInput(String prompt) throws RemoteException{
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            return scanner.nextLine();
        }
    // Other user-related methods as needed
}

