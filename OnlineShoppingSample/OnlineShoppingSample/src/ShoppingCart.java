import java.util.ArrayList;
import java.util.List;

class ShoppingCart {
    private List<Product> productList;

    public ShoppingCart() {
        this.productList = new ArrayList<>();
    }

    public void addProductToCart(Product product) {
        productList.add(product);
    }

    public double calculateTotalCost() {
        double totalCost = 0;
        for (Product product : productList) {
            totalCost += product.getPrice() * product.getQuantity();
        }
        return totalCost;
    }

    public boolean isEligibleForDiscount() {
        // Check if the cart is eligible for the first purchase discount
        return productList.isEmpty();
    }

    public boolean hasCategoryDiscount() {
        // Check if the cart has at least three products of the same category
        int electronicsCount = 0;
        int clothingCount = 0;

        for (Product product : productList) {
            if (product instanceof Electronics) {
                electronicsCount++;
            } else if (product instanceof Clothing) {
                clothingCount++;
            }
        }

        return electronicsCount >= 3 || clothingCount >= 3;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
