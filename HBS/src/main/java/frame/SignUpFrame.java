package frame;

import javax.swing.*;

import services.Store;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField phoneField;
    private JTextField idCardField;
    private JButton signUpButton;
    private LoginFrame loginFrame;

    public SignUpFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame; 

        setTitle("Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2)); 

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        add(dobField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("ID Card:"));
        idCardField = new JTextField();
        add(idCardField);

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        add(signUpButton);

        setVisible(true);
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String dob = dobField.getText();
        String phone = phoneField.getText();
        String idCard = idCardField.getText();
        String role = "Customer"; 

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || dob.isEmpty() || phone.isEmpty() || idCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone.length() > 11) {
            JOptionPane.showMessageDialog(this, "Phone number must be less than or equal to 11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@")) {
            String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }


            String sqlInsert = "INSERT INTO users (username, password, name, dob, phone, id_card, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, name);
                preparedStatement.setString(4, dob);
                preparedStatement.setString(5, phone);
                preparedStatement.setString(6, idCard);
                preparedStatement.setString(7, role); 

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");

                loginFrame.setVisible(true);
                dispose(); 
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1292) { 
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter a valid date (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred during registration!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




    public static void main(String[] args) {
        new SignUpFrame(new LoginFrame(new Store()));
    }
}
