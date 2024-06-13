import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class WestminsterShoppingManager implements ShoppingManager, Serializable {
    private static final long serialVersionUID = 1L;

    private List<Product> inventory;

    public WestminsterShoppingManager() {
        this.inventory = new ArrayList<>();
    }

    @Override
    public void addProductToInventory(Product product) {
        inventory.add(product);
    }

    @Override
    public void removeProductFromInventory(String productID) {
        inventory.removeIf(product -> product.getProductID().equals(productID));
    }

    @Override
    public List<Product> getInventory() {
        return inventory;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Add Product to Inventory");
        System.out.println("2. Remove Product from Inventory");
        System.out.println("3. Display Products");
        System.out.println("4. Save Products to File");
        System.out.println("5. Load Products from File");
        System.out.println("6. Switch to GUI");
        System.out.println("7. Close Application");

    }

    @Override
    public void displayProducts() {
        Collections.sort(inventory, (p1, p2) -> p1.getProductID().compareTo(p2.getProductID()));

        for (Product product : inventory) {
            System.out.println(product);
            System.out.println("------------------------------");
        }
    }

    @Override
    public String createFileInProjectDirectory(String productFile) {
        try {
            File file = new File(productFile);
            if (file.createNewFile()) {
                System.out.println("File created in the project directory: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            } else {
                System.out.println("File already exists in the project directory: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveProductsToFile(String productFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(productFile))) {
            oos.writeObject(inventory);
            System.out.println("Products saved to file: " + productFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadProductsFromFile(String productFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(productFile))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                inventory = (List<Product>) obj;
                System.out.println("Products loaded from file: " + productFile);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
