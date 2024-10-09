package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Person {
    private String id;
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

}
