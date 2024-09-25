package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Book;
import model.Product;
import model.Stationary;
import model.Toy;
import services.Store;

public class EmployeeFrame extends JFrame {
    private Store store;
    private LoginFrame loginFrame;
    private JList<String> productList;
    private DefaultListModel<String> productListModel;
    private JTextField searchField;
    private JLabel suggestionLabel;

    public EmployeeFrame(Store store, LoginFrame loginFrame) {
        this.store = store;
        this.loginFrame = loginFrame;

        setTitle("Employee Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        suggestionLabel = new JLabel(""); // Initialize the suggestion label
        searchPanel.add(suggestionLabel); // Add suggestion label to the search panel
        add(searchPanel, BorderLayout.NORTH);

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(productList), BorderLayout.CENTER);

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

        // Add document listener to update suggestions as you type
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        editProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProduct();
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProduct();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        loadProducts(); // Load products at startup
    }

    private void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            suggestionLabel.setText(""); 
            return;
        }

        List<Product> products = store.getProducts();
        List<String> suggestions = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase())
                        || (product instanceof Book
                                && (((Book) product).getAuthor().toLowerCase().contains(query.toLowerCase())
                                || ((Book) product).getIsbn().toLowerCase().contains(query.toLowerCase())))
                        || (product instanceof Toy
                                && ((Toy) product).getBrand().toLowerCase().contains(query.toLowerCase()))
                        || (product instanceof Stationary
                                && ((Stationary) product).getBrand().toLowerCase().contains(query.toLowerCase())))
                .map(product -> String.format("%s (ID: %s)", product.getName(), product.getId())) // Include ID in suggestions
                .collect(Collectors.toList());

        if (!suggestions.isEmpty()) {
            suggestionLabel.setText("Suggestions: " + String.join(", ", suggestions));
        } else {
            suggestionLabel.setText("No suggestions available.");
        }
    }

    private void loadProducts() {
        productListModel.clear(); 
        List<Product> products = store.getProducts();

        for (Product product : products) {
            String displayInfo = getProductDisplayInfo(product);
            productListModel.addElement(displayInfo);
        }
    }

    private String getProductDisplayInfo(Product product) {
        if (product instanceof Book) {
            Book book = (Book) product;
            return String.format("Book - ID: %s, Name: %s, Price: %.2f, Author: %s, ISBN: %s, Quantity: %d",
                    book.getId(), book.getName(), book.getPrice(), book.getAuthor(), book.getIsbn(),
                    book.getQuantity());
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            return String.format("Toy - ID: %s, Name: %s, Price: %.2f, Brand: %s, Suitable Age: %d, Quantity: %d",
                    toy.getId(), toy.getName(), toy.getPrice(), toy.getBrand(), toy.getSuitAge(),
                    toy.getQuantity());
        } else if (product instanceof Stationary) {
            Stationary stationary = (Stationary) product;
            return String.format("Stationary - ID: %s, Name: %s, Price: %.2f, Brand: %s, Quantity: %d",
                    stationary.getId(), stationary.getName(), stationary.getPrice(), stationary.getBrand(),
                    stationary.getQuantity());
        }
        return ""; 
    }

    private void addProduct() {
        String id = JOptionPane.showInputDialog(this, "Enter Product ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter Product Price:");
        String quantityStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
        String type = JOptionPane.showInputDialog(this, "Enter Product Type (Book/Toy/Stationery):");

        if (id != null && name != null && priceStr != null && quantityStr != null && type != null) {
            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                Product product = null;

                switch (type) {
                    case "Book":
                        String author = JOptionPane.showInputDialog(this, "Enter Author:");
                        String publisher = JOptionPane.showInputDialog(this, "Enter Publisher:");
                        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
                        int publicationYear = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Publication Year:"));
                        product = new Book(id, name, price, quantity, author, isbn, publicationYear, publisher);
                        break;
                    case "Toy":
                        String brand = JOptionPane.showInputDialog(this, "Enter Brand:");
                        String material = JOptionPane.showInputDialog(this, "Enter Material:");
                        int suitage = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Suitable Age:"));
                        product = new Toy(id, name, price, quantity, brand, suitage, material);
                        break;
                    case "Stationery":
                        String brandsta = JOptionPane.showInputDialog(this, "Enter Brand:");
                        String typesta = JOptionPane.showInputDialog(this, "Enter Type of Stationery:");
                        String materialsta = JOptionPane.showInputDialog(this, "Enter Material:");
                        product = new Stationary(id, name, price, quantity, brandsta, typesta, materialsta);
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
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
            return;
        }

        String selectedProductInfo = productListModel.get(selectedIndex);
        String id = selectedProductInfo.split(",")[1].split(":")[1].trim(); 

        String newName = JOptionPane.showInputDialog(this, "Enter new Product Name:");
        String newPriceStr = JOptionPane.showInputDialog(this, "Enter new Product Price:");
        String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new Quantity:");

        if (newName != null && newPriceStr != null && newQuantityStr != null) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                int newQuantity = Integer.parseInt(newQuantityStr);

                Product existingProduct = store.getProductById(id); 
                if (existingProduct != null) {
                    existingProduct.setName(newName);
                    existingProduct.setPrice(newPrice);
                    existingProduct.setQuantity(newQuantity);

                    store.updateProduct(existingProduct); 
                    loadProducts();
                    JOptionPane.showMessageDialog(this, "Product updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found.");
                }
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
        String id = selectedProductInfo.split(",")[1].split(":")[1].trim(); // Extract the product ID from the selected product

        store.removeProduct(id); // Pass the ID (String) instead of the Product object
        loadProducts(); // Reload the product list
        JOptionPane.showMessageDialog(this, "Product removed successfully.");
    }

    private void logout() {
        setVisible(false);
        loginFrame.setVisible(true);
    }
}
