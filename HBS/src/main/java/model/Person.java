package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {
    private String id;        // Assuming id is stored as a String
    private String username;
    private String password;
    private String name;
    private String role;

    // Constructor to initialize a Person object
    public Person(String id, String username, String password, String name, String role) {
        this.id = id;
        this.username = username; 
        this.password = password;
        this.name = name;
        this.role = role; 
    }

    // Getters for the fields
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void addUser(Connection connection) {
        String sql = "INSERT INTO users (id, username, name, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, this.id);
            stmt.setString(2, this.username);
            stmt.setString(3, this.name);
            stmt.setString(4, this.password);
            stmt.setString(5, this.role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
