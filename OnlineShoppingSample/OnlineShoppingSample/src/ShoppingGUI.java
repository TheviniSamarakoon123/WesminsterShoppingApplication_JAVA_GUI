import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShoppingGUI extends JFrame {
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewCartButton;

    private WestminsterShoppingManager shoppingManager;
    private ShoppingCart shoppingCart;
    private List<Product> productList;
    private DefaultTableModel tableModel;

    public ShoppingGUI(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.shoppingCart = new ShoppingCart();
        this.productList = new ArrayList<>();

        initializeGUI();
        loadProductsToTable();
    }
    
    private void initializeGUI() {
        setTitle("Shopping GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"});
        productTable = new JTable();
        productDetailsTextArea = new JTextArea();
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");

        tableModel = new DefaultTableModel();
        productTable.setModel(tableModel);
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Available Items");
        tableModel.addColumn("Price");
        tableModel.addColumn("Info");

        updateProductTable("All");

        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) productTypeComboBox.getSelectedItem();
                updateProductTable(selectedType);
            }
        });

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                showProductDetails(productList.get(selectedRow));
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Product selectedProduct = productList.get(selectedRow);
                    int quantity = askForQuantity();
                    if (quantity > 0 && quantity <= selectedProduct.getAvailableItems()) {
                        selectedProduct.setAvailableItems(selectedProduct.getAvailableItems() - quantity);
                        selectedProduct.setQuantity(quantity);
                        shoppingCart.addProductToCart(selectedProduct);
                        addProductToTable(selectedProduct);
                        JOptionPane.showMessageDialog(ShoppingGUI.this, "Product added to the cart!");
                        updateProductTable((String) productTypeComboBox.getSelectedItem());
                    } else {
                        JOptionPane.showMessageDialog(ShoppingGUI.this, "Invalid quantity entered.");
                    }
                }
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShoppingCart();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Product Type:"));
        topPanel.add(productTypeComboBox);
        topPanel.add(viewCartButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(new JScrollPane(productTable));
        centerPanel.add(new JScrollPane(productDetailsTextArea));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(addToCartButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void addProductToTable(Product product) {
        Object[] rowData = {product.getProductID(), product.getProductName(),
                product.getAvailableItems(), product.getPrice(), getInfoString(product)};
        tableModel.addRow(rowData);

        int rowIndex = tableModel.getRowCount() - 1;
        int availableItems = (int) tableModel.getValueAt(rowIndex, 2);

        // Highlight row with reduced availability
        if (availableItems < 3) {
            SwingUtilities.invokeLater(() -> {
                tableModel.fireTableCellUpdated(rowIndex, 0);
                tableModel.fireTableCellUpdated(rowIndex, 1);
                tableModel.fireTableCellUpdated(rowIndex, 2);
                tableModel.fireTableCellUpdated(rowIndex, 3);
                tableModel.fireTableCellUpdated(rowIndex, 4);
            });
        }
    }

    private String getInfoString(Product product) {
        if (product instanceof Electronics electronicProduct) {
            return "Brand: " + electronicProduct.getBrand() + ", Warranty: " + electronicProduct.getWarrantyPeriod() + " months";
        } else if (product instanceof Clothing clothingProduct) {
            return "Size: " + clothingProduct.getSize() + ", Color: " + clothingProduct.getColor();
        }
        return "";
    }

    private int askForQuantity() {
        String quantityStr = JOptionPane.showInputDialog(ShoppingGUI.this, "Enter quantity:");
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }

    private void loadProductsToTable() {

        tableModel.setRowCount(0);
        // Populate the table with products from WestminsterShoppingManager
        List<Product> productList = shoppingManager.getInventory();
        for (Product product : productList) {
            addProductToTable(product);
        }
        productTable.setModel(tableModel);
    }

    private void updateProductTable(String productType) {
        tableModel.setRowCount(0);

        for (Product product : productList) {
            if (productType.equals("All") || (productType.equals("Electronics") && product instanceof Electronics)
                    || (productType.equals("Clothes") && product instanceof Clothing)) {
                addProductToTable(product);
            }
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int availableItems = (int) tableModel.getValueAt(i, 2);
            if (availableItems < 3) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    tableModel.fireTableCellUpdated(i, j);
                }
            }
        }
    }

    private void showProductDetails(Product product) {
        productDetailsTextArea.setText(product.toString());
    }

    private void showShoppingCart() {
        JFrame cartFrame = new JFrame("Shopping Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a table model for the selected products
        DefaultTableModel selectedProductsModel = new DefaultTableModel();
        JTable selectedProductsTable = new JTable(selectedProductsModel);
        selectedProductsModel.addColumn("Product Details");
        selectedProductsModel.addColumn("Quantity");
        selectedProductsModel.addColumn("Price");

        // Add selected products to the table
        List<Product> cartItems = shoppingCart.getProductList();
        for (Product product : cartItems) {
            selectedProductsModel.addRow(new Object[]{
                    product.getProductID() + " - " + product.getProductName() + "\n" + getInfoString(product),
                    product.getQuantity(),  // Corrected this line
                    product.getPrice() * product.getQuantity()
            });
        }

        // Create a text area to show the price breakdown
        JTextArea priceBreakdownTextArea = new JTextArea();
        priceBreakdownTextArea.setEditable(false);

        // Calculate total cost
        double totalCost = shoppingCart.calculateTotalCost();

        // Apply discounts
        if (shoppingCart.isEligibleForDiscount()) {
            totalCost *= 0.9;
        }
        if (shoppingCart.hasCategoryDiscount()) {
            totalCost *= 0.8;
        }

        // Display price breakdown
        priceBreakdownTextArea.append("Total Cost: $" + totalCost + "\n");
        priceBreakdownTextArea.append("Discount Applied: $" + (totalCost * 0.1) + " (10% off for being eligible)\n");
        if (shoppingCart.hasCategoryDiscount()) {
            priceBreakdownTextArea.append("Category Discount Applied: $" + (totalCost * 0.2) + " (20% off for 3 items in the same category)\n");
        }

        // Add components to the cart frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(selectedProductsTable), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(priceBreakdownTextArea), BorderLayout.SOUTH);
        cartFrame.add(mainPanel);

        cartFrame.setVisible(true);
    }

}
