package model;

public class Book extends Product {
	private String author;
	private String isbn;
	private int publicationYear; 
	private String publisher; 

	public Book(String id, String name, double price, int quantity, String author, String isbn, int publicationYear, String publisher) {
		super(id, name, price, quantity);
		this.author = author;
		this.isbn = isbn;
		this.publicationYear = publicationYear; // Initialize publication year
		this.publisher = publisher; // Initialize publisher
	}

	// Getters
	public String getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getPublicationYear() {
		return publicationYear; // Getter for publication year
	}

	public String getPublisher() {
		return publisher; // Getter for publisher
	}

	// Setters
	public void setAuthor(String author) {
		this.author = author;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher; // Setter for publisher
	}

	@Override
	public String toString() {
		return "Book [ID=" + getId() + ", Name=" + getName() + ", Price=" + getPrice() + ", Author=" + author
				+ ", ISBN=" + isbn + ", Publication Year=" + publicationYear + ", Publisher=" + publisher + "]";
	}
}
