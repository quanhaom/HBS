package services;
import java.sql.Types;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.Toy;
import model.Stationery;
import model.Person;
import model.Product;

public class Store {
    private Connection connection;

    public Store() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@"); // Update with your credentials
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getUsers() {
        List<Person> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Person person = new Person(rs.getString("username"), rs.getString("name"), rs.getString("password"), rs.getString("role"));
                users.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
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

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String type = rs.getString("type");
                Product product;

                switch (type) {
                    case "Toy":
                        product = new Toy(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("brand"),
                            rs.getInt("suitage"),
                            rs.getString("material")
                        );
                        break;
                    case "Stationery":
                        product = new Stationery(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("brand"),
                            rs.getString("material")
                        );
                        break;
                    case "Book":
                        product = new Book(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getInt("publication_year"),
                            rs.getString("publisher")
                        );
                        break;
                    default:
                        continue;
                }
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(String productId) {
        Product product = null; 
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { 
                    String type = rs.getString("type");

                    switch (type) {
                        case "Toy":
                            product = new Toy(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("quantity"),
                                rs.getString("brand"),
                                rs.getInt("suitage"),
                                rs.getString("material")
                            );
                            break;
                        case "Stationery":
                            product = new Stationery(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("quantity"),
                                rs.getString("brand"),
                                rs.getString("material")
                            );
                            break;
                        case "Book":
                            product = new Book(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("quantity"),
                                rs.getString("author"),
                                rs.getString("isbn"),
                                rs.getInt("publication_year"),
                                rs.getString("publisher")
                            );
                            break;
                        default:
                            // Handle unexpected types
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product; 
    }
    public void removeProduct(String productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId); 
            int rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Product> searchByAuthor(String author) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE author = ?"; 

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author); 
            ResultSet rs = stmt.executeQuery(); 
            
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String authorName = rs.getString("author");
                Product product = new Book(id, name, price, quantity, authorName, rs.getString("isbn"), 
                                           rs.getInt("publication_year"), rs.getString("publisher"));
                productList.add(product); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        
        return productList; 
    }
    public List<Product> searchByName(String name) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?"; 

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%"); 
            ResultSet rs = stmt.executeQuery(); 
            
            while (rs.next()) {
                String id = rs.getString("id");
                String productName = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Product product = new Product(id, productName, price, quantity);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        
        return productList; 
    }
    public List<Book> searchByPublicationYear(int year) {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE publication_year = ?"; 

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, year); 
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                int publicationYear = rs.getInt("publication_year");
                String publisher = rs.getString("publisher");

                Book book = new Book(id, name, price, quantity, author, isbn, publicationYear, publisher);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookList; 
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ?, author = ?, isbn = ?, publisher = ?, brand = ?, material = ? WHERE id = ?"; // SQL query to update product details

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName()); 
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity()); 

            if (product instanceof Book) {
                Book book = (Book) product;
                stmt.setString(4, book.getAuthor());
                stmt.setString(5, book.getIsbn()); 
                stmt.setString(6, book.getPublisher()); 
                stmt.setNull(7, Types.VARCHAR); 
                stmt.setNull(8, Types.VARCHAR); 
            } else if (product instanceof Toy) {
                Toy toy = (Toy) product;
                stmt.setNull(4, Types.VARCHAR); 
                stmt.setNull(5, Types.VARCHAR); 
                stmt.setNull(6, Types.VARCHAR); 
                stmt.setString(7, toy.getBrand()); 
                stmt.setString(8, toy.getMaterial());
            } else if (product instanceof Stationery) {
                Stationery stationery = (Stationery) product;
                stmt.setNull(4, Types.VARCHAR); 
                stmt.setNull(5, Types.VARCHAR); 
                stmt.setNull(6, Types.VARCHAR);
                stmt.setString(7, stationery.getBrand());
                stmt.setString(8, stationery.getMaterial()); 
            } else {
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.VARCHAR); 
                stmt.setNull(6, Types.VARCHAR); 
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR); 
            }

            stmt.setString(9, product.getId());

            int rowsAffected = stmt.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    public int getNextProductId() {
        String sqlMaxId = "SELECT COALESCE(MAX(id), 0) FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sqlMaxId);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; 
    }
}


