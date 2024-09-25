package model;

public class Product {

}
package model;

public class Product {
	private String id;
	private String name;
	private double price;
	private int quantity;

	public Product(String id, String name, double price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	// Getters
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	// Setters
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() { // Add this method
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	// Override toString method
	@Override
	public String toString() {
		return "Product [ID=" + id + ", Name=" + name + ", Price=" + price + "]";
	}
}
