package model;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private double inputprice;

    public Product(String id, String name, double price, int quantity, double inputprice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.inputprice = inputprice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public double getInputPrice() {
    	return inputprice;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setInputPrice(double inputprice) {
    	this.inputprice = inputprice;
    }
    
}
