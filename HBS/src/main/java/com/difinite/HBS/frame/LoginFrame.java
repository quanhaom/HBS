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
	private Store store; // Reference to the Store class

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
		JButton clearButton = new JButton("Clear");

		// Set up layout
		setLayout(new GridLayout(3, 2));
		add(userIdLabel);
		add(userIdField);
		add(passwordLabel);
		add(passwordField);
		add(loginButton);
		add(clearButton);

		// Action listeners
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login(userIdField.getText(), new String(passwordField.getPassword()));
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userIdField.setText("");
				passwordField.setText("");
			}
		});
	}

	private void login(String userId, String password) {
		List<Person> users = store.getUsers(); // Get all users from the store
		for (Person user : users) {
			if (user.getId().equals(userId) && user.getPassword().equals(password)) {
				// Handle role-based navigation
				switch (user.getRole()) {
				case "Employee":
					new EmployeeFrame(store, this).setVisible(true); // Pass LoginFrame reference
					break;
				case "Customer":
					new CustomerFrame(store, this).setVisible(true);
					break;
				case "Manager":
					new ManagerFrame(store, this).setVisible(true);
					break;
				default:
					JOptionPane.showMessageDialog(this, "Unknown role: " + user.getRole());
					return;
				}
				dispose(); // Close LoginFrame
				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
	}

	public static void main(String[] args) {
		Store store = new Store(); // Initialize Store

		// Populate store with users (for testing)
		store.addUser(new Person("1", "Alice", "1", "Customer"));
		store.addUser(new Person("2", "Bob", "2", "Employee"));
		store.addUser(new Person("3", "Charlie", "3", "Manager"));

		LoginFrame loginFrame = new LoginFrame(store);
		loginFrame.setVisible(true);
	}
}
