package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.Person;
import services.Store;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private Store store; 

    public LoginFrame(Store store) {
        this.store = store;

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create UI components
        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton dontLoginButton = new JButton("Don't Login");
        JButton signUpButton = new JButton("Sign Up");

        // Set up layout
        setLayout(new GridLayout(4, 2)); 
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(dontLoginButton);
        add(signUpButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure this call to login is within the LoginFrame class
                login(userIdField.getText(), new String(passwordField.getPassword()));
            }
        });

        dontLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StrangerFrame(store , null);
                dispose();
                return;
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpFrame(LoginFrame.this); // Pass the current instance
                dispose();
            }
        });


    }
    	

    private void login(String userId, String password) {
        List<Person> users = store.getUsers(); 
        for (Person user : users) {
            if (user.getId().equals(userId) && user.getPassword().equals(password)) {
                switch (user.getRole()) {
                    case "Employee":
                        new EmployeeFrame(store, this).setVisible(true);
                        break;
                    case "Customer":
                        new CustomerFrame(store, this).setVisible(true);
                        break;
                    case "Manager":
                        new ManagerFrame(store,this).setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown role: " + user.getRole());
                        return;
                }
                dispose(); 
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
    }

    public static void main(String[] args) {
        Store store = new Store(); 

        LoginFrame loginFrame = new LoginFrame(store); // Create LoginFrame
        loginFrame.setVisible(true); // Make frame visible
    }
}
