package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Product;
import services.Store;

public class CustomerFrame extends BaseFrame {
    private List<Product> cart;
    private LoginFrame loginFrame;

    public CustomerFrame(Store store, LoginFrame loginFrame) {
        super(store);  
        this.cart = new ArrayList<>(); 
        this.loginFrame = loginFrame; 

        setTitle("Customer Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Check out");
        JButton logoutButton = new JButton("Log out");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
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

        displayAllProducts();
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
