package frame;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Book;
import model.Product;
import model.Stationery;
import model.Toy;
import services.Store;

public class EmployeeFrame extends BaseFrame {

    private LoginFrame loginFrame;

    public EmployeeFrame(Store store, LoginFrame loginFrame) {
        super(store);  // Call BaseFrame constructor
        this.loginFrame = loginFrame;

        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button Panel for Product Actions
        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton editProductButton = new JButton("Edit Product");
        JButton removeProductButton = new JButton("Remove Product");
        JButton logoutButton = new JButton("Logout");
        buttonPanel.add(addProductButton);
        buttonPanel.add(editProductButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners for buttons
        addProductButton.addActionListener(e -> addProduct());
        editProductButton.addActionListener(e -> editProduct());
        removeProductButton.addActionListener(e -> removeProduct());
        logoutButton.addActionListener(e -> logout());

        loadProducts(); // Load products when frame opens
    }

    private void addProduct() {
        String type = JOptionPane.showInputDialog(this, "Enter Product Type (Book/Toy/Stationery):");
        String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter Product Price:");
        String quantityStr = JOptionPane.showInputDialog(this, "Enter Quantity:");

        if (name != null && priceStr != null && quantityStr != null && type != null) {
            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                Product product = null;

                // Automatically generate ID
                int id = store.getNextProductId();  // Method to get the next product ID

                switch (type) {
                    case "Book":
                        String author = JOptionPane.showInputDialog(this, "Enter Author:");
                        String publisher = JOptionPane.showInputDialog(this, "Enter Publisher:");
                        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
                        int publicationYear = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Publication Year:"));
                        product = new Book(String.valueOf(id), name, price, quantity, author, isbn, publicationYear, publisher);
                        break;
                    case "Toy":
                        String brand = JOptionPane.showInputDialog(this, "Enter Brand:");
                        String material = JOptionPane.showInputDialog(this, "Enter Material:");
                        int suitage = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Suitable Age:"));
                        product = new Toy(String.valueOf(id), name, price, quantity, brand, suitage, material); // Ensure material is passed here
                        break;
                    case "Stationery":
                        String brandsta = JOptionPane.showInputDialog(this, "Enter Brand:");
                        String materialsta = JOptionPane.showInputDialog(this, "Enter Material:");
                        product = new Stationery(String.valueOf(id), name, price, quantity, brandsta, materialsta);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Invalid product type.");
                        return;
                }

                store.addProduct(product);  // Add product to store
                loadProducts();  // Reload the product list
                JOptionPane.showMessageDialog(this, "Product added successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for price or quantity.");
            }
        }
    }

    private void editProduct() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
            return;
        }

        String selectedProductInfo = productListModel.get(selectedIndex);
        String[] parts = selectedProductInfo.split(","); 
        String id = parts[0].split(":")[1].trim();

        // Get the current product information from the store
        Product existingProduct = store.getProductById(id);
        if (existingProduct == null) {
            JOptionPane.showMessageDialog(this, "Product not found.");
            return;
        }

        // Prompt for common product attributes
        String newName = JOptionPane.showInputDialog(this, "Enter new Product Name:", existingProduct.getName());
        String newPriceStr = JOptionPane.showInputDialog(this, "Enter new Product Price:", existingProduct.getPrice());
        String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new Quantity:", existingProduct.getQuantity());

        if (newName != null && newPriceStr != null && newQuantityStr != null) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                int newQuantity = Integer.parseInt(newQuantityStr);

                existingProduct.setName(newName);
                existingProduct.setPrice(newPrice);
                existingProduct.setQuantity(newQuantity);

                // Update specific attributes based on product type
                if (existingProduct instanceof Book) {
                    Book book = (Book) existingProduct;
                    String newAuthor = JOptionPane.showInputDialog(this, "Enter new Author:", book.getAuthor());
                    String newIsbn = JOptionPane.showInputDialog(this, "Enter new ISBN:", book.getIsbn());
                    String newPublisher = JOptionPane.showInputDialog(this, "Enter new Publisher:", book.getPublisher());

                    if (newAuthor != null && newIsbn != null && newPublisher != null) {
                        book.setAuthor(newAuthor);
                        book.setIsbn(newIsbn);
                        book.setPublisher(newPublisher);
                    }
                } else if (existingProduct instanceof Toy) {
                    Toy toy = (Toy) existingProduct;
                    String newBrand = JOptionPane.showInputDialog(this, "Enter new Brand:", toy.getBrand());
                    String newMaterial = JOptionPane.showInputDialog(this, "Enter new Material:", toy.getMaterial());

                    if (newBrand != null && newMaterial != null) {
                        toy.setBrand(newBrand);
                        toy.setMaterial(newMaterial);
                    }
                } else if (existingProduct instanceof Stationery) {
                    Stationery stationery = (Stationery) existingProduct;
                    String newBrand = JOptionPane.showInputDialog(this, "Enter new Brand:", stationery.getBrand());
                    String newMaterial = JOptionPane.showInputDialog(this, "Enter new Material:", stationery.getMaterial());

                    if (newBrand != null && newMaterial != null) {
                        stationery.setBrand(newBrand);
                        stationery.setMaterial(newMaterial);
                    }
                }

                store.updateProduct(existingProduct);
                loadProducts();
                JOptionPane.showMessageDialog(this, "Product updated successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for price or quantity.");
            }
        }
    }



    private void removeProduct() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove.");
            return;
        }

        String selectedProductInfo = productListModel.get(selectedIndex);
        String[] parts = selectedProductInfo.split(","); 
        String id = parts[0].split(":")[1].trim(); 
        String name = parts[1].split(":")[1].trim(); 

        store.removeProduct(id);  
        loadProducts(); 
        JOptionPane.showMessageDialog(this,  name + " removed successfully."); 
    }


    private void logout() {
        setVisible(false);
        loginFrame.setVisible(true);
    }

    private void loadProducts() {
        displayAllProducts(); 
    }
}
