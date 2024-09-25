package model;

public class Stationary extends Product {
	private String brand;
	private String type;
	private String material;

	public Stationary(String id, String name, double price, int quantity, String brand, String type, String material) {
		super(id, name, price, quantity);
		this.brand = brand;
		this.type = type;
		this.material = material;
	}

	public String getBrand() {
		return brand;
	}

	public String getType() {
		return type;
	}

	public String getMaterial() {
		return material;
	}

	// Setters
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	@Override
	public String toString() {
		return "Stationary [ID=" + getId() + ", Name=" + getName() + ", Price=" + getPrice() + ", Quantity=" + getQuantity()
				+ ", Brand=" + getBrand() + ", Type=" + getType() + ", Material=" + getMaterial() + "]";
	}
}
