package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {
	private String id;
	private String name;
	private String password;
	private String role;

	public Person(String id, String name, String password, String role) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role; 
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
	}
	public void addUser(Connection connection) {
        String sql = "INSERT INTO users (id, name, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, this.id);
            stmt.setString(2, this.name);
            stmt.setString(3, this.password);
            stmt.setString(4, this.role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
