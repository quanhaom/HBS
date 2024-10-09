package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import services.DatabaseConnection;

public class Employee extends Person {
    private double salary1;
    private double salary2;
    private double salary3;
    private String dob;
    private String phone;
    private String idCard;
    private int workingHours;
    private Connection connection;
    public Employee(String id, String username, String password,String name, String role, String dob, String phone, String idCard, int workingHours,double salary1,double salary2,double salary3,DatabaseConnection dbConnection) {
        super(id, username, password,name, role);
        this.dob = dob;
        this.phone = phone;
        this.idCard = idCard;
        this.workingHours = workingHours;
        this.salary1 = salary1;

            try {
                this.connection = dbConnection.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            
        
            	}
        }           

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
    public double getSalary1() {
    	return salary1;
    }
    public double getSalary2() {
    	return salary2;
    }
    public double getSalary3() {
    	return salary3;
    }
    public void addProduct(Product product) {
        String sqlMaxId = "SELECT MAX(id) FROM products";
        String sqlInsert = "INSERT INTO products (id, name, price, quantity, brand, suitage, material, type, author, isbn, publication_year, publisher) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 

        try {
            int newId = 1; 
            try (PreparedStatement stmtMax = connection.prepareStatement(sqlMaxId);
                 ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt(1) + 1; // Increment max id by 1
                }
            }
            
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, newId); 
                stmtInsert.setString(2, product.getName());
                stmtInsert.setDouble(3, product.getPrice());
                stmtInsert.setInt(4, product.getQuantity());

                if (product instanceof Toy) {
                    Toy toy = (Toy) product;
                    stmtInsert.setString(5, toy.getBrand()); 
                    stmtInsert.setInt(6, toy.getSuitAge()); 
                    stmtInsert.setString(7, toy.getMaterial()); 
                    stmtInsert.setString(8, "Toy"); 
                    stmtInsert.setNull(9, Types.VARCHAR); 
                    stmtInsert.setNull(10, Types.VARCHAR); 
                    stmtInsert.setNull(11, Types.INTEGER); 
                    stmtInsert.setNull(12, Types.VARCHAR);
                }
                	else if (product instanceof Stationery) {
                    Stationery stationery = (Stationery) product;
                    stmtInsert.setString(5, stationery.getBrand());
                    stmtInsert.setNull(6, Types.INTEGER); 
                    stmtInsert.setString(7, stationery.getMaterial());
                    stmtInsert.setString(8,"Stationery"); 
                    stmtInsert.setNull(9, Types.VARCHAR); 
                    stmtInsert.setNull(10, Types.VARCHAR); 
                    stmtInsert.setNull(11, Types.INTEGER); 
                    stmtInsert.setNull(12, Types.VARCHAR); 
                } else if (product instanceof Book) {
                    Book book = (Book) product;
                    stmtInsert.setNull(5, Types.VARCHAR); 
                    stmtInsert.setNull(6, Types.INTEGER); 
                    stmtInsert.setNull(7, Types.VARCHAR); 
                    stmtInsert.setString(8, "Book"); 
                    stmtInsert.setString(9, book.getAuthor());
                    stmtInsert.setString(10, book.getIsbn());
                    stmtInsert.setInt(11, book.getPublicationYear());
                    stmtInsert.setString(12, book.getPublisher());
                }

                stmtInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    


    @Override
    public String toString() {
        return "Employee [ID=" + getId() + ", Name=" + getName() + ", Role=" + getRole() + 
               ", DOB=" + dob + ", Phone=" + phone + 
               ", ID Card=" + idCard + ", Working Hours=" + workingHours + "]";
    }
}
