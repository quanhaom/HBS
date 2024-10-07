package model;

public class Toy extends Product {
	private String brand;
	private int suitage;
	private String material;

	public Toy(String id, String name, double price, int quantity, String brand, int suitage, String material) {
		super(id, name, price, quantity);
		this.brand = brand;
		this.suitage = suitage;
		this.material = material;
	}

	public String getBrand() {
		return brand;
	}

	public int getSuitAge() {
		return suitage;
	}
	public String getMaterial() {
		return material;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setSuitAge(int suitage) {
		this.suitage = suitage;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	
}
