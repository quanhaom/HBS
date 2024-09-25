package model;

public class Person {
	private String id;
	private String name;
	private String password;
	private String role;

	public Person(String id, String name, String password, String role) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role; // Initialize role
	}

	// Getters
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	} // Ensure this is correctly implemented
}
