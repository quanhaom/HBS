package frame;

import model.Person;
import services.Store;

public class Main {
	public static void main(String[] args) {
		Store store = new Store(); // Initialize Store

		// Populate store with users (for testing)
		store.addUser(new Person("1", "Alice", "1", "Customer"));
		store.addUser(new Person("2", "Bob", "2", "Employee"));
		store.addUser(new Person("3", "Charlie", "3", "Manager"));

		// Launch the login frame
		LoginFrame loginFrame = new LoginFrame(store);
		loginFrame.setVisible(true);
	}
}
