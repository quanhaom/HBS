package frame;

import java.awt.GridLayout;
import java.sql.*;
import javax.swing.*;

public class EditPro5 extends JFrame {
    private JTextField idField, usernameField, passwordField, nameField, dobField, phoneField, idCardField, pointField, roleField;
    private JButton applyButton, cancelButton;
    private String userId;
    private String role;

    public EditPro5(String userId) {
        this.userId = userId;
        setTitle("Edit Profile");
        setLayout(new GridLayout(10, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        add(idField);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JTextField();
        add(passwordField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Role:"));
        roleField = new JTextField();
        roleField.setEditable(false);
        add(roleField); 

        add(new JLabel("DOB (YYYY-MM-DD):"));
        dobField = new JTextField();
        add(dobField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("ID Card:"));
        idCardField = new JTextField();
        add(idCardField);

        add(new JLabel("Points:"));
        pointField = new JTextField();
        pointField.setEditable(false);
        add(pointField);

        applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applyChanges());
        add(applyButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());
        add(cancelButton);
        
        loadUserInfo();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadUserInfo() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                idField.setText(resultSet.getString("id"));
                usernameField.setText(resultSet.getString("username"));
                passwordField.setText(resultSet.getString("password"));
                nameField.setText(resultSet.getString("name"));
                dobField.setText(resultSet.getString("dob"));
                phoneField.setText(resultSet.getString("phone"));
                idCardField.setText(resultSet.getString("id_card"));
                
                role = resultSet.getString("role");
                roleField.setText(role);
                
                String points = resultSet.getString("point");
                pointField.setText(points != null ? points : "0");
                pointField.setEditable(false);

                if ("customer".equalsIgnoreCase(role)) {
                    usernameField.setEditable(false);
                    passwordField.setEditable(false);
                    nameField.setEditable(false);
                    dobField.setEditable(false);
                    phoneField.setEditable(false);
                    idCardField.setEditable(false);
                } 
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyChanges() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String dob = dobField.getText();
        String phone = phoneField.getText();
        String idCard = idCardField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || dob.isEmpty() || phone.isEmpty() || idCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields except Points.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phone.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Phone number must have 10 or 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidDate(dob)) {
            JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD and must be a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update the user's information in the database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET username=?, password=?, name=?, dob=?, phone=?, id_card=? WHERE id=?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, dob);
            statement.setString(5, phone);
            statement.setString(6, idCard);
            statement.setString(7, userId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating profile. No rows affected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDate(String date) {
        // Validate the date format
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!date.matches(regex)) {
            return false;
        }

        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            return false;
        }

        int[] daysInMonth = {31, (isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day > 0 && day <= daysInMonth[month - 1];
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void cancel() {
        dispose();
    }
}

