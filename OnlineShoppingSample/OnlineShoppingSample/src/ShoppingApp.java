import javax.swing.*;
import java.util.Scanner;

public class ShoppingApp {
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Shopping App!");
        System.out.println("Choose an option:");
        System.out.println("1. Console Menu");
        System.out.println("2. User Mode");

        int choice = scanner.nextInt();

        if (choice == 1) {
            // Console Mode
            interactWithConsole(shoppingManager);
        } else if (choice == 2) {
            // User Menu
            //SwingUtilities.invokeLater(() -> new ShoppingGUI(shoppingManager));
            // Validate user login
            boolean validLogin = validateUserLogin();

            if (validLogin) {
                SwingUtilities.invokeLater(() -> new ShoppingGUI(shoppingManager));
            } else {
                System.out.println("Invalid login. Exiting the application.");
            }

        } else {
            System.out.println("Invalid choice. Exiting the application.");
        }
    }

    private static void interactWithConsole(WestminsterShoppingManager shoppingManager) {
        Scanner scanner = new Scanner(System.in);
        int option;

        String filePath = shoppingManager.createFileInProjectDirectory("products.dat");

        do {
            shoppingManager.displayMenu();
            System.out.print("Enter your choice: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    // Add Product to Inventory
                    addProductToInventory(shoppingManager);
                    break;
                case 2:
                    // Remove Product from Inventory
                    removeProductFromInventory(shoppingManager);
                    break;
                case 3:
                    // Display Products
                    shoppingManager.displayProducts();
                    break;
                case 4:
                    // Save Products to File
                    shoppingManager.saveProductsToFile(filePath);
                    break;
                case 5:
                    // Load Products from File
                    shoppingManager.loadProductsFromFile(filePath);
                    break;
                case 6:
                    SwingUtilities.invokeLater(() -> new ShoppingGUI(shoppingManager));
                    break;
                case 7:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (option != 7);
    }

    private static void addProductToInventory(WestminsterShoppingManager shoppingManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter product details:");
        System.out.print("Product ID: ");
        String productID = scanner.next();
        System.out.print("Product Type (Electronics-->E/e or Clothing-->C/c): ");
        String productType = scanner.next();

        // Common attributes
        System.out.print("Product Name: ");
        String productName = scanner.next();
        System.out.print("Available Items: ");
        int availableItems = scanner.nextInt();
        System.out.print("Price: ");
        double price = scanner.nextDouble();

        if (productType.equalsIgnoreCase("E")||productType.equalsIgnoreCase("e")) {
            // Electronics-specific attributes
            System.out.print("Brand: ");
            String brand = scanner.next();
            System.out.print("Warranty Period (in months): ");
            int warrantyPeriod = scanner.nextInt();

            shoppingManager.addProductToInventory(new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod));
        } else if (productType.equalsIgnoreCase("C")||productType.equalsIgnoreCase("c")) {
            // Clothing-specific attributes
            System.out.print("Size: ");
            String size = scanner.next();
            System.out.print("Color: ");
            String color = scanner.next();

            shoppingManager.addProductToInventory(new Clothing(productID, productName, availableItems, price, size, color));
        } else {
            // invalid product type
            System.out.println("Invalid product type. Product not added to inventory.");
            return;
        }

        System.out.println("Product added to inventory.");
    }

    private static void removeProductFromInventory(WestminsterShoppingManager shoppingManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the product ID to remove: ");
        String productID = scanner.next();

        shoppingManager.removeProductFromInventory(productID);
        System.out.println("Product removed from inventory.");
    }

    private static boolean validateUserLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String enteredUsername = scanner.next();
        System.out.print("Enter password: ");
        String enteredPassword = scanner.next();

        // Assuming you have a User instance, replace 'user' with your actual User instance
        User user = new User("abc", "123");

        // Validate login
        return user.validateLogin(enteredUsername, enteredPassword);
    }
}


