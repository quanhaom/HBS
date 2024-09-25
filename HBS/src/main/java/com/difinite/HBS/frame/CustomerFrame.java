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
import model.MovieDisc;
import model.MusicDisc;
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
	// Method to search products by multiple attributes (case insensitive)
	// Method to search products by multiple attributes (case insensitive)
	private void searchProducts(String query) {
		productListModel.clear(); // Clear previous results
		List<Product> products = store.getProducts();
		String lowerCaseQuery = query.toLowerCase(); // Convert query to lower case for case-insensitive comparison

		for (Product product : products) {
			String displayInfo = getProductDisplayInfo(product);
			String productType = product instanceof Book ? "Book"
					: product instanceof MovieDisc ? "Movie" : product instanceof MusicDisc ? "Music" : "";

			// Additional checks for search
			if (product.getId().toLowerCase().contains(lowerCaseQuery)
					|| product.getName().toLowerCase().contains(lowerCaseQuery)
					|| productType.toLowerCase().contains(lowerCaseQuery) // Check for product type
					|| (product instanceof Book && (((Book) product).getAuthor().toLowerCase().contains(lowerCaseQuery)
							|| ((Book) product).getIsbn().toLowerCase().contains(lowerCaseQuery)))
					|| (product instanceof MovieDisc
							&& ((MovieDisc) product).getDirector().toLowerCase().contains(lowerCaseQuery))) {
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
		} else if (product instanceof MovieDisc) {
			MovieDisc movie = (MovieDisc) product;
			return String.format("Movie - ID: %s, Name: %s, Price: %.2f, Director: %s, Duration: %d mins, Quantity: %d",
					movie.getId(), movie.getName(), movie.getPrice(), movie.getDirector(), movie.getDuration(),
					movie.getQuantity());
		} else if (product instanceof MusicDisc) {
			MusicDisc music = (MusicDisc) product;
			return String.format("Music - ID: %s, Name: %s, Price: %.2f, Artist: %s, Quantity: %d", music.getId(),
					music.getName(), music.getPrice(), music.getArtist(), music.getQuantity());
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
	// Method to update search suggestions
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
						|| (product instanceof MovieDisc
								&& ((MovieDisc) product).getDirector().toLowerCase().contains(query.toLowerCase()))
						|| (product instanceof MusicDisc
								&& ((MusicDisc) product).getArtist().toLowerCase().contains(query.toLowerCase()))
						|| (product instanceof Book && "Book".toLowerCase().contains(query.toLowerCase())) // Check for
																											// product
																											// type
						|| (product instanceof MovieDisc && "Movie".toLowerCase().contains(query.toLowerCase())) // Check
																													// for
																													// product
																													// type
						|| (product instanceof MusicDisc && "Music".toLowerCase().contains(query.toLowerCase())) // Check
																													// for
																													// product
																													// type
				).map(Product::getName).collect(Collectors.toList());

		if (!suggestions.isEmpty()) {
			suggestionLabel.setText("Suggestions: " + String.join(", ", suggestions));
		} else {
			suggestionLabel.setText("No suggestions available.");
		}
	}

	// Method to add selected product to cart
	private void addToCart() {
		String selectedProduct = productList.getSelectedValue(); // Get the selected product
		if (selectedProduct != null && !selectedProduct.equals("No products found.")) {
			// Extract product ID from the selected line
			String productId = selectedProduct.split(", ")[1].split(": ")[1]; // Get the product ID
			for (Product product : store.getProducts()) {
				if (product.getId().equals(productId)) {
					cart.add(product); // Add to cart
					JOptionPane.showMessageDialog(this, product.getName() + " has been added to your cart.");
					return;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a valid product to add to cart.");
		}
	}

	// Method to view cart contents
	private void viewCart() {
		if (cart.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Your cart is empty.");
			return;
		}

		StringBuilder cartContents = new StringBuilder("Your Cart:\n");
		for (Product product : cart) {
			cartContents.append(product.getName()).append("\n");
		}
		JOptionPane.showMessageDialog(this, cartContents.toString());
	}

	// Method for checkout
	private void checkout() {
		if (cart.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Your cart is empty.");
			return;
		}

		// Handle checkout logic here, e.g., processing payment
		JOptionPane.showMessageDialog(this, "Checkout successful!");
		cart.clear(); // Clear the cart after successful checkout
	}

	// Method for logging out
	private void logout() {
		this.dispose(); // Close the CustomerFrame
		loginFrame.setVisible(true); // Show the login frame again
	}
}
