package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hbs_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "iuhuyenlemleM0@"; // Update with your credentials

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
