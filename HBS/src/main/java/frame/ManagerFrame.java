package frame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import services.Store;

public class ManagerFrame extends JFrame {
    private Store store; // Store reference for accessing data
    private LoginFrame loginFrame;
    
    public ManagerFrame(Store store,LoginFrame loginFrame) { // Fix the constructor parameter name
        this.store = store;
        this.loginFrame=loginFrame;

        setTitle("Manager Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton productsButton = new JButton("Products");
        JButton employeesButton = new JButton("Employees");
        JButton storeButton = new JButton("Store");
        JButton logoutButton = new JButton("Logout");
        
        buttonPanel.add(productsButton);
        buttonPanel.add(employeesButton);
        buttonPanel.add(storeButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.CENTER);
        

        logoutButton.addActionListener(e -> logout());
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProducts();
            }
        });

        employeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmployees();
            }
        });

        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStore();
            }
        });
    }

    // Method to show the products management interface
    private void showProducts() {
        ProductFrame productFrame = new ProductFrame(store, this); // Correctly passing the instance
        productFrame.setVisible(true);
        this.setVisible(false); // Hide the ManagerFrame when showing ProductFrame
    }



    // Method to show the employees management interface
    private void showEmployees() {
    	EmpmanaFrame empmanaFrame = new EmpmanaFrame(); // Correctly passing the instance
        empmanaFrame.setVisible(true);
        this.setVisible(false);
    }

    // Method to show the store information interface
    private void showStore() {
        JOptionPane.showMessageDialog(this, "Store Information");
        // Implement functionality to show store details here
        // e.g., displaying store statistics or information
    }
    private void logout() {
        setVisible(false);
		loginFrame.setVisible(true);}

    // Main method for testing
}
