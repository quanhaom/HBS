package services;

import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.Toy;
import model.Stationary;
import model.Person;
import model.Product;

public class Store {
	private List<Person> users; // List of users (customers, employees, managers)
	private List<Product> products; // List of products available in the store

	public Store() {
		users = new ArrayList<>();
		products = new ArrayList<>();
		initializeProducts(); // Call the method to add sample products
	}

	// Method to add a user (Person)
	public void addUser(Person person) {
		users.add(person);
	}

	// Method to get all users
	public List<Person> getUsers() {
		return users;
	}

	// Method to add a product
	public void addProduct(Product product) {
		products.add(product);
	}

	// Method to get all products
	public List<Product> getProducts() {
		return products;
	}

	// Method to search products by name
	public List<Product> searchByName(String name) {
		List<Product> foundProducts = new ArrayList<>();
		for (Product product : products) {
			if (product.getName().equalsIgnoreCase(name)) {
				foundProducts.add(product);
			}
		}
		return foundProducts;
	}

	// Method to search books by author
	public List<Book> searchByAuthor(String author) {
		List<Book> foundBooks = new ArrayList<>();
		for (Product product : products) {
			if (product instanceof Book && ((Book) product).getAuthor().equalsIgnoreCase(author)) {
				foundBooks.add((Book) product);
			}
		}
		return foundBooks;
	}

	// Method to search books by publication year
	public List<Book> searchByPublicationYear(int year) {
		List<Book> foundBooks = new ArrayList<>();
		for (Product product : products) {
			if (product instanceof Book && ((Book) product).getPublicationYear() == year) {
				foundBooks.add((Book) product);
			}
		}
		return foundBooks;
	}

	public Product getProductById(String id) {
		for (Product product : products) {
			if (product.getId().equals(id)) {
				return product; // Return product if ID matches
			}
		}
		return null; // Return null if no product matches
	}

	public boolean updateProductQuantity(String id, int newQuantity) {
		for (Product product : products) {
			if (product.getId().equals(id)) {
				product.setQuantity(newQuantity);
				return true; // Indicate success
			}
		}
		return false; // Indicate failure (ID not found)
	}

	public boolean removeProduct(String id) {
		Product productToRemove = getProductById(id);
		if (productToRemove != null) {
			products.remove(productToRemove);
			return true; // Indicate success
		}
		return false; // Indicate failure (ID not found)
	}

	public void updateProduct(Product updatedProduct) {
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getId().equals(updatedProduct.getId())) {
				products.set(i, updatedProduct);
				return; // Exit once the product is updated
			}
		}
		// Optionally, throw an exception or log if the product is not found
	}

	// Method to initialize sample products
	private void initializeProducts() {
	    // Adding sample books
	    products.add(new Book("1", "Effective Java", 45.00, 10, "Joshua Bloch", "978-0134686097", 2018, "Prentice Hall"));
	    products.add(new Book("2", "Clean Code", 40.00, 8, "Robert C. Martin", "978-0136083238", 2008, "Prentice Hall"));
	    products.add(new Book("3", "The Pragmatic Programmer", 42.00, 15, "Andrew Hunt", "978-0135957059", 2019, "Addison-Wesley"));
	    products.add(new Book("4", "The Art of Computer Programming", 100.00, 5, "Donald Knuth", "978-0201896831", 1998, "Addison-Wesley"));
	    products.add(new Book("5", "Introduction to Algorithms", 85.00, 12, "Thomas H. Cormen", "978-0262033848", 2009, "MIT Press"));

	    // Adding sample stationary items
	    products.add(new Stationary("6", "Pencil", 1.50, 100, "Faber-Castell", "Pencil", "Wood"));
	    products.add(new Stationary("7", "Notebook", 3.00, 50, "Moleskine", "Notebook", "Paper"));
	    products.add(new Stationary("8", "Eraser", 0.75, 200, "Staedtler", "Eraser", "Rubber"));
	    products.add(new Stationary("9", "Ruler", 1.20, 150, "Westcott", "Ruler", "Plastic"));
	    products.add(new Stationary("10", "Highlighter", 2.00, 80, "Sharpie", "Highlighter", "Ink"));

	    // Adding sample toys
	    products.add(new Toy("11", "Action Figure", 20.00, 30, "Hasbro", 5, "Plastic"));
	    products.add(new Toy("12", "Building Blocks", 15.00, 25, "Lego", 3, "Plastic"));
	    products.add(new Toy("13", "Doll House", 60.00, 10, "Barbie", 6, "Wood"));
	    products.add(new Toy("14", "Race Car", 25.00, 20, "Hot Wheels", 4, "Metal"));
	    products.add(new Toy("15", "Puzzle", 10.00, 50, "Melissa & Doug", 3, "Cardboard"));
	}

}
