import java.util.List;

interface ShoppingManager {
    void addProductToInventory(Product product);

    void removeProductFromInventory(String productID);

    List<Product> getInventory();

    void displayMenu();

    void displayProducts();

    void saveProductsToFile(String filePath);

    void loadProductsFromFile(String filePath);

    String createFileInProjectDirectory(String productFile);
}
