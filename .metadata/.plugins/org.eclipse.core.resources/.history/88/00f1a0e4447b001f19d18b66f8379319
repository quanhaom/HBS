package model;

import java.util.List;

import services.Store;

public class Customer extends Person {
	private Store store; // Reference to the store

	public Customer(String id, String name, String password, Store store) {
		super(id, name, password, "Customer"); // Call the constructor of Person with role
		this.store = store; // Initialize store reference
	}

	// Method to search for products by name
	public List<Product> searchProductsByName(String name) {
		return store.searchByName(name);
	}

	// Method to search for books by author
	public List<Book> searchBooksByAuthor(String author) {
		return store.searchByAuthor(author);
	}

	// Method to search for books by publication year
	public List<Book> searchBooksByPublicationYear(int year) {
		return store.searchByPublicationYear(year);
	}
}
