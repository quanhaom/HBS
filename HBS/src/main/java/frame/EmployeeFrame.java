package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import model.Toy; // Changed from MovieDisc to Toy
import model.Stationary; // Changed from MusicDisc to Stationary
import model.Product;
import services.Store;

public class CustomerFrame extends JFrame {
    private Store store;
    private JList<String> productList; // Use JList for product selection
    private DefaultListModel<String> productListModel; // Model for the JList
    private JTextField searchField;
    private List<Product> cart; // List to hold cart items
    private JLabel suggestionLabel; // Label to show search suggestions
    private LoginFrame loginFrame; // Reference to LoginFrame

    // Constructor with Store and LoginFrame reference
    public CustomerFrame(Store store, LoginFrame loginFrame) {
        this.store = store;
        this.cart = new ArrayList<>(); // Initialize cart
        this.loginFrame = loginFrame; // Store the reference to the login frame

        setTitle("Customer Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Layout
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        suggestionLabel = new JLabel(); // Initialize suggestion label
        searchPanel.add(suggestionLabel);
        add(searchPanel, BorderLayout.NORTH);

        // Product List
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow single selection
        add(new JScrollPane(productList), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("Xem Giỏ Hàng");
        JButton checkoutButton = new JButton("Thanh Toán");
        JButton logoutButton = new JButton("Đăng Xuất"); // Add Logout Button
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(logoutButton); // Add the logout button to the panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts(searchField.getText());
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });

        logoutButton.addActionListener(new ActionListener() { // Handle Logout
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Document Listener for search suggestions
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

        // Initialize product display on startup
        displayAllProducts();
    }

    // Method to search products by multiple attributes (case insensitive)
    private void searchProducts(String query) {
        productListModel.clear(); // Clear previous results
        List<Product> products = store.getProducts();
        String lowerCaseQuery = query.toLowerCase(); // Convert query to lower case for case-insensitive comparison

        for (Product product : products) {
            String displayInfo = getProductDisplayInfo(product);
            String productType = product instanceof Book ? "Book"
                    : product instanceof Toy ? "Toy" : product instanceof Stationary ? "Stationary" : ""; // Updated here

            // Additional checks for search
            if (product.getId().toLowerCase().contains(lowerCaseQuery)
                    || product.getName().toLowerCase().contains(lowerCaseQuery)
                    || productType.toLowerCase().contains(lowerCaseQuery) // Check for product type
                    || (product instanceof Book && (((Book) product).getAuthor().toLowerCase().contains(lowerCaseQuery)
                            || ((Book) product).getIsbn().toLowerCase().contains(lowerCaseQuery)))
                    || (product instanceof Toy
                            && ((Toy) product).getToyType().toLowerCase().contains(lowerCaseQuery)) // Assuming getToyType() exists
                    || (product instanceof Stationary
                            && ((Stationary) product).getBrand().toLowerCase().contains(lowerCaseQuery)) // Assuming getBrand() exists
                    ) {
                productListModel.addElement(displayInfo);
            }
        }

        if (productListModel.isEmpty()) {
            productListModel.addElement("No products found.");
        }
    }

    // Method to get product display information including more details
    private String getProductDisplayInfo(Product product) {
        if (product instanceof Book) {
            Book book = (Book) product;
            return String.format("Book - ID: %s, Name: %s, Price: %.2f, Author: %s, ISBN: %s, Quantity: %d",
                    book.getId(), book.getName(), book.getPrice(), book.getAuthor(), book.getIsbn(),
                    book.getQuantity());
        } else if (product instanceof Toy) { // Updated here
            Toy toy = (Toy) product; // Assuming Toy has appropriate fields
            return String.format("Toy - ID: %s, Name: %s, Price: %.2f, Type: %s, Quantity: %d",
                    toy.getId(), toy.getName(), toy.getPrice(), toy.getToyType(), // Assuming getToyType() exists
                    toy.getQuantity());
        } else if (product instanceof Stationary) { // Updated here
            Stationary stationary = (Stationary) product; // Assuming Stationary has appropriate fields
            return String.format("Stationary - ID: %s, Name: %s, Price: %.2f, Brand: %s, Quantity: %d",
                    stationary.getId(), stationary.getName(), stationary.getPrice(), stationary.getBrand(), // Assuming getBrand() exists
                    stationary.getQuantity());
        }
        return ""; // Return empty string if product type is unknown
    }

    // Method to display all products
    private void displayAllProducts() {
        productListModel.clear(); // Clear previous content
        List<Product> products = store.getProducts();

        for (Product product : products) {
            String displayInfo = getProductDisplayInfo(product);
            productListModel.addElement(displayInfo);
        }
    }

    // Method to update search suggestions
    private void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            suggestionLabel.setText(""); // Clear suggestions if query is empty
            return;
        }

        List<Product> products = store.getProducts();
        List<String> suggestions = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase())
                        || (product instanceof Book
                                && (((Book) product).getAuthor().toLowerCase().contains(query.toLowerCase())
                                        || ((Book) product).getIsbn().toLowerCase().contains(query.toLowerCase())))
                        || (product instanceof Toy
                                && ((Toy) product).getToyType().toLowerCase().contains(query.toLowerCase())) // Assuming getToyType() exists
                        || (product instanceof Stationary
                                && ((Stationary) product).getBrand().toLowerCase().contains(query.toLowerCase())) // Assuming getBrand() exists
                        || (product instanceof Book && "Book".toLowerCase().contains(query.toLowerCase())) // Check for product type
                        || (product instanceof Toy && "Toy".toLowerCase().contains(query.toLowerCase())) // Check for product type
                        || (product instanceof Stationary && "Stationary".toLowerCase().contains(query.toLowerCase())) // Check for product type
                ).map(Product::getName).collect(Collectors.toList());

        if (!suggestions.isEmpty()) {
            suggestionLabel.setText("Suggestions: " + String.join(", ", suggestions));
        } else {
            suggestionLabel.setText("No suggestions available.");
        }
    }

    // Method to add selected product to cart
    private void addToCart() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedProductInfo = productListModel.getElementAt(selectedIndex);
            String productId = selectedProductInfo.split(",")[0].split(":")[1].trim(); // Extract ID from the display info

            List<Product> products = store.getProducts();
            for (Product product : products) {
                if (product.getId().equals(productId)) {
                    cart.add(product);
                    JOptionPane.showMessageDialog(this, "Added to cart: " + product.getName());
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to cart.");
        }
    }

    // Method to view cart items
    private void viewCart() {
        StringBuilder cartContents = new StringBuilder("Items in your cart:\n");
        for (Product product : cart) {
            cartContents.append(product.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, cartContents.toString());
    }

    // Method to handle checkout
    private void checkout() {
        // Implement checkout logic here
        JOptionPane.showMessageDialog(this, "Proceeding to checkout...");
    }

    // Method to handle logout
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?");
        if (confirm == JOptionPane.YES_OPTION) {
            loginFrame.setVisible(true); // Show the login frame again
            dispose(); // Close the current frame
        }
    }
}
