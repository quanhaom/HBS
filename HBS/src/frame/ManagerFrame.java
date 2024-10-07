package frame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import services.Store;

public class ManagerFrame extends JFrame {
    private Store store; 
    private LoginFrame loginFrame;
    
    public ManagerFrame(Store store,LoginFrame loginFrame,String userName) { 
        this.store = store;
        this.loginFrame=loginFrame;

        setTitle("Manager Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        JButton productsButton = new JButton("Products");
        JButton employeesButton = new JButton("Users");
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

    private void showProducts() {
        ProductFrame productFrame = new ProductFrame(store, this);
        productFrame.setVisible(true);
        this.setVisible(false);
    }

    private void showEmployees() {
    	EmpmanaFrame empmanaFrame = new EmpmanaFrame(this);
        empmanaFrame.setVisible(true);
        this.setVisible(false);
    }

    private void showStore() {
        JOptionPane.showMessageDialog(this, "Store Information");
    }
    private void logout() {
        setVisible(false);
		loginFrame.setVisible(true);}

}
