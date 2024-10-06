package frame;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Book;
import model.Product;
import model.Stationery;
import model.Toy;
import services.Store;

public class EmployeeFrame extends BaseFrame {

    private LoginFrame loginFrame;
    private JLabel greetingLabel;
    private String Name;
    private String userId;

    public EmployeeFrame(Store store, LoginFrame loginFrame, String userId, String Name) {
        super(store); 
        this.loginFrame = loginFrame;
        this.userId = userId;
        this.Name = Name;
        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel greetingPanel = new JPanel();
        if (Name == null) {
           
        } else {
            greetingLabel = new JLabel("Hello, " + Name);
            greetingPanel.add(greetingLabel);
            greetingLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    showGreetingOptions();
                }
            });
        }

        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton editProductButton = new JButton("Edit Product");
        JButton removeProductButton = new JButton("Remove Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(editProductButton);
        buttonPanel.add(removeProductButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(greetingPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.SOUTH);

        // Action listeners
        addProductButton.addActionListener(e -> addProduct());
        editProductButton.addActionListener(e -> editProduct());
        removeProductButton.addActionListener(e -> removeProduct());

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
    private void showGreetingOptions() {
        String[] options = {"Log out", "Edit Profile"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an option:", "Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            logout();
        } else if (choice == 1) {
            openEditProfile();
        }
    }
    public void openEditProfile() {
        new EditPro5( userId);
    }
}
