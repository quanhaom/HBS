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
    private String userName;

    public EmployeeFrame(Store store, LoginFrame loginFrame,String username) {
        super(store); 
        this.loginFrame = loginFrame;
        this.userName = userName;

        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        addProductButton.addActionListener(e -> addProduct());
        editProductButton.addActionListener(e -> editProduct());
        removeProductButton.addActionListener(e -> removeProduct());
        logoutButton.addActionListener(e -> logout());

        loadProducts();
    }

    private void addProduct() {
    	String[] types = {"Book", "Toy", "Stationery"};
        String type = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role Selection",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter Product Price:");
        String quantityStr = JOptionPane.showInputDialog(this, "Enter Quantity:");

        if (name != null && priceStr != null && quantityStr != null && type != null) {
            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                Product product = null;

                int id = store.getNextProductId(); 

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
                        product = new Toy(String.valueOf(id), name, price, quantity, brand, suitage, material); 
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

                store.addProduct(product); 
                loadProducts();
                JOptionPane.showMessageDialog(this, "Product added successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for price or quantity.");
            }
        }
    }

    private void editProduct() {
        int selectedRow = productTable.getSelectedRow(); 

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);

        Product existingProduct = store.getProductById(id);
        if (existingProduct == null) {
            JOptionPane.showMessageDialog(this, "Product not found.");
            return;
        }

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
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0); 
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + name + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            store.removeProduct(id);
            loadProducts(); 
            JOptionPane.showMessageDialog(this, name + " removed successfully."); 
        }
    }



    private void logout() {
        setVisible(false);
        loginFrame.setVisible(true);
    }

    private void loadProducts() {
        displayAllProducts(); 
    }
}
