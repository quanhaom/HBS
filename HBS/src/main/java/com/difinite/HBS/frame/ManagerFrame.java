package frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import services.Store;

public class ManagerFrame extends JFrame {
	private Store store; // Reference to the Store class
	private JFrame loginFrame; // Reference to LoginFrame

	public ManagerFrame(Store store, JFrame loginFrame) {
		this.store = store;
		this.loginFrame = loginFrame;

		setTitle("Manager Dashboard");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Center the frame

		// Example content for ManagerFrame
		JPanel panel = new JPanel();
		panel.add(new JLabel("Welcome to the Manager Dashboard!"));

		// Add a logout button (if needed)
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(e -> logout());
		panel.add(logoutButton);

		add(panel);
		setVisible(true);
	}

	private void logout() {
		this.dispose(); // Close the ManagerFrame
		loginFrame.setVisible(true); // Show the login frame again
	}
}
