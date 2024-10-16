package frame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox; // Import JComboBox
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Book;
import model.Product;
import model.Stationery;
import services.Store;
import model.Toy;

public abstract class BaseFrame extends JFrame {
    protected Store store;
    protected JList<String> productList; 
    protected DefaultListModel<String> productListModel; 
    protected JTextField searchField;
    protected JLabel suggestionLabel;
    protected JComboBox<String> sortOptions; 
    protected static final int WIDTH = 1000;  
    protected static final int HEIGHT = 700;

    public BaseFrame(Store store) {
        this.store = store;
        setSize(WIDTH, HEIGHT);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL url = getClass().getClassLoader().getResource("icon.png"); 
        if (url != null) {
            Image icon = Toolkit.getDefaultToolkit().getImage(url);
            setIconImage(icon);
        } else {
            System.err.println("Icon not found!");
        }
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        searchField = new JTextField(15);
        searchPanel.add(searchField, gbc);
        
        String[] sortingCriteria = { "ID", "Price", "Quantity", "Product Type" };
        sortOptions = new JComboBox<>(sortingCriteria);
        
        sortOptions.addActionListener(e -> updateSortedProductDisplay());
        
        gbc.gridx = 2; 
        searchPanel.add(sortOptions, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = 1;
        suggestionLabel = new JLabel();
        searchPanel.add(suggestionLabel, gbc);
        
        add(searchPanel, BorderLayout.NORTH);

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        add(new JScrollPane(productList), BorderLayout.CENTER);

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

    protected String getProductDisplayInfo(Product product) {
        if (product instanceof Book) {
            Book book = (Book) product;
            return String.format("Book - ID: %s, Name: %s, Price: %.2f, Author: %s, ISBN: %s, Publication Year: %d, Publisher: %s, Quantity: %d",
                    book.getId(), book.getName(), book.getPrice(), book.getAuthor(), book.getIsbn(),
                    book.getPublicationYear(), book.getPublisher(), book.getQuantity());
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            return String.format("Toy - ID: %s, Name: %s, Price: %.2f, Brand: %s, Suitable Age: %d, Material: %s, Quantity: %d",
                    toy.getId(), toy.getName(), toy.getPrice(), toy.getBrand(), toy.getSuitAge(),
                    toy.getMaterial(), toy.getQuantity());
        } else if (product instanceof Stationery) {
            Stationery stationery = (Stationery) product;
            return String.format("Stationery - ID: %s, Name: %s, Price: %.2f, Brand: %s, Material: %s, Quantity: %d",
                    stationery.getId(), stationery.getName(), stationery.getPrice(), stationery.getBrand()
                    , stationery.getMaterial(), stationery.getQuantity());
        }
        
        return "Unknown product type";
    }


    protected void displayAllProducts() {
        productListModel.clear();
        List<Product> products = store.getProducts();
        
        // Sort products based on the selected criteria
        products = sortProducts(products);
        
        for (Product product : products) {
            String displayInfo = getProductDisplayInfo(product);
            productListModel.addElement(displayInfo);
        }
    }

    protected List<Product> sortProducts(List<Product> products) {
        String selectedCriteria = (String) sortOptions.getSelectedItem();

        return products.stream()
                .sorted((p1, p2) -> {
                    switch (selectedCriteria) {
                        case "ID":
                            // Parse IDs as integers for proper numeric sorting
                            return Integer.compare(Integer.parseInt(p1.getId()), Integer.parseInt(p2.getId()));
                        case "Price":
                            return Double.compare(p1.getPrice(), p2.getPrice());
                        case "Quantity":
                            return Integer.compare(p1.getQuantity(), p2.getQuantity());
                        case "Product Type":
                            return getProductType(p1).compareTo(getProductType(p2));
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }


    private String getProductType(Product product) {
        if (product instanceof Book) {
            return "Book";
        } else if (product instanceof Toy) {
            return "Toy";
        } else if (product instanceof Stationery) {
            return "Stationery";
        }
        return "";
    }

    private void updateSortedProductDisplay() {
        displayAllProducts(); 
    }

    protected void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        productListModel.clear();  

        if (query.isEmpty()) {
            suggestionLabel.setText(""); 
            displayAllProducts(); 
            return;
        }
        
        List<Product> products = store.getProducts();
        List<Product> filteredProducts = products.stream()
            .filter(product -> 
                product.getId().toLowerCase().contains(query.toLowerCase()) || 
                product.getName().toLowerCase().contains(query.toLowerCase()) ||  
                String.valueOf(product.getPrice()).toLowerCase().contains(query.toLowerCase()) ||
                String.valueOf(product.getQuantity()).toLowerCase().contains(query.toLowerCase()) ||
                (product instanceof Book
                    && (((Book) product).getAuthor().toLowerCase().contains(query.toLowerCase())
                    || ((Book) product).getIsbn().toLowerCase().contains(query.toLowerCase()))) ||
                (product instanceof Toy
                    && (((Toy) product).getBrand().toLowerCase().contains(query.toLowerCase())
                    || ((Toy) product).getMaterial().toLowerCase().contains(query.toLowerCase()))) ||
                (product instanceof Stationery
                    && (((Stationery) product).getBrand().toLowerCase().contains(query.toLowerCase())
                    || ((Stationery) product).getMaterial().toLowerCase().contains(query.toLowerCase()))) || 
                (product instanceof Book && "Book".toLowerCase().contains(query.toLowerCase())) ||
                (product instanceof Toy && "Toy".toLowerCase().contains(query.toLowerCase())) ||
                (product instanceof Stationery && "Stationery".toLowerCase().contains(query.toLowerCase()))
            )
            .collect(Collectors.toList());



        if (!filteredProducts.isEmpty()) {
            for (Product product : filteredProducts) {
                String displayInfo = getProductDisplayInfo(product); 
                productListModel.addElement(displayInfo);  
            }

        } else {
            productListModel.addElement("No products found.");  
        }
    }
}
