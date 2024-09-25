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
import model.Stationary;
import model.Toy;
import model.Product;
import services.Store;

public class CustomerFrame extends JFrame {
	private Store store;
	private JList<String> productList; 
	private DefaultListModel<String> productListModel; 
	private JTextField searchField;
	private List<Product> cart; 
	private JLabel suggestionLabel;
	private LoginFrame loginFrame; 

	public CustomerFrame(Store store, LoginFrame loginFrame) {
		this.store = store;
		this.cart = new ArrayList<>(); 
		this.loginFrame = loginFrame; 

		setTitle("Customer Frame");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setLayout(new BorderLayout());

		JPanel searchPanel = new JPanel();
		searchPanel.add(new JLabel("Search:"));
		searchField = new JTextField(20);
		searchPanel.add(searchField);
		JButton searchButton = new JButton("Search");
		searchPanel.add(searchButton);
		suggestionLabel = new JLabel();
		searchPanel.add(suggestionLabel);
		add(searchPanel, BorderLayout.NORTH);

		productListModel = new DefaultListModel<>();
		productList = new JList<>(productListModel);
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(productList), BorderLayout.CENTER);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		JButton addToCartButton = new JButton("Add to Cart");
		JButton viewCartButton = new JButton("Xem Giỏ Hàng");
		JButton checkoutButton = new JButton("Thanh Toán");
		JButton logoutButton = new JButton("Đăng Xuất");
		buttonPanel.add(addToCartButton);
		buttonPanel.add(viewCartButton);
		buttonPanel.add(checkoutButton);
		buttonPanel.add(logoutButton);
		add(buttonPanel, BorderLayout.SOUTH);

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

		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});

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

		displayAllProducts();
	}

	private void searchProducts(String query) {
	    productListModel.clear(); // Clea r previous results
	    List<Product> products = store.getProducts();
	    String lowerCaseQuery = query.toLowerCase(); 

	    for (Product product : products) {
	        String displayInfo = getProductDisplayInfo(product);
	        String productType = product instanceof Book ? "Book"
	                : product instanceof Toy ? "Toy" : product instanceof Stationary ? "Stationary" : "";

	        // Additional checks for search
	        if (product.getId().toLowerCase().contains(lowerCaseQuery)
	                || product.getName().toLowerCase().contains(lowerCaseQuery)
	                || productType.toLowerCase().contains(lowerCaseQuery) // Check for product type
	                || (product instanceof Book && (((Book) product).getAuthor().toLowerCase().contains(lowerCaseQuery)
	                        || ((Book) product).getIsbn().toLowerCase().contains(lowerCaseQuery))) // Check for author and ISBN
	                || (product instanceof Toy && ((Toy) product).getBrand().toLowerCase().contains(lowerCaseQuery))) {
	            productListModel.addElement(displayInfo);
	        }
	    }

	    if (productListModel.isEmpty()) {
	        productListModel.addElement("No products found.");
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
			return String.format("Stationary - ID: %s, Name: %s, Price: %.2f, Brand: %s, Quantity: %d", stationary.getId(),
					stationary.getName(), stationary.getPrice(), stationary.getBrand(), stationary.getQuantity());
		}
		return ""; 
	}


	private void displayAllProducts() {
		productListModel.clear(); // Clear previous content
		List<Product> products = store.getProducts();

		for (Product product : products) {
			String displayInfo = getProductDisplayInfo(product);
			productListModel.addElement(displayInfo);
		}
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
	                            && ((Stationary) product).getBrand().toLowerCase().contains(query.toLowerCase()))
	                    || (product instanceof Book && "Book".toLowerCase().contains(query.toLowerCase()))
	                    || (product instanceof Toy && "Toy".toLowerCase().contains(query.toLowerCase()))
	                    || (product instanceof Stationary && "Stationary".toLowerCase().contains(query.toLowerCase())) 
	            )
	            .map(Product::getName)
	            .collect(Collectors.toList());

	    if (!suggestions.isEmpty()) {
	        suggestionLabel.setText("Suggestions: " + String.join(", ", suggestions));
	    } else {
	        suggestionLabel.setText("No suggestions available.");
	    }
	}

	private void addToCart() {
	    String selectedProduct = productList.getSelectedValue();
	    if (selectedProduct != null && !selectedProduct.equals("No products found.")) {
	        String productId = selectedProduct.split(", ")[0].split(": ")[1];
	        for (Product product : store.getProducts()) {
	            if (product.getId().equals(productId)) {
	                cart.add(product);
	                JOptionPane.showMessageDialog(this, product.getName() + " has been added to your cart.");
	                return;
	            }
	        }
	        JOptionPane.showMessageDialog(this, "Product not found in the store.");
	    } else {
	        JOptionPane.showMessageDialog(this, "Please select a valid product to add to cart.");
	    }
	}



	private void viewCart() {
	    if (cart.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Your cart is empty.");
	        return;
	    }

	    StringBuilder cartContents = new StringBuilder("Your Cart:\n");
	    for (Product product : cart) {
	        cartContents.append(String.format("%s - Price: %.2f\n", product.getName(), product.getPrice()));
	    }
	    JOptionPane.showMessageDialog(this, cartContents.toString());
	}
	private void checkout() {
		if (cart.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Your cart is empty.");
			return;
		}

		JOptionPane.showMessageDialog(this, "Checkout successful!");
		cart.clear();
	}

	private void logout() {
		this.dispose(); 
		loginFrame.setVisible(true);
	}
}
