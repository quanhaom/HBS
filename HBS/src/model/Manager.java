package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import services.MySQLConnection;

public class Manager extends Employee {

    public Manager(String id, String username, String password, String name, String role,
                   String dob, String phone, String idCard, int workingHours,
                   double salary1, double salary2, double salary3) {
        super(id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3);
    }

    public Manager() {
        super("0", "default", "default", "default", "Manager", "0000-00-00", "0000000000", "default", 0, 0.0, 0.0, 0.0);
    }

	public double calculateTotalCompensation() {
        return getSalary1() + getSalary2() + getSalary3(); 
    }
    public void addUser(DefaultTableModel tableModel) {
        String sqlMaxId = "SELECT MAX(id) FROM users";
        String id = "1";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@")) {
            try (PreparedStatement statement = connection.prepareStatement(sqlMaxId);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int maxId = resultSet.getInt(1);
                    id = String.valueOf(maxId + 1);
                }
            }

            String username = JOptionPane.showInputDialog(null, "Enter Username:");
            String password = JOptionPane.showInputDialog(null, "Enter Password:");
            String name = JOptionPane.showInputDialog(null, "Enter Name:");

            String[] roles = {"Manager", "Customer", "Employee"};
            String role = (String) JOptionPane.showInputDialog(null, "Select Role:", "Role Selection",
                    JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

            String dob;
            boolean isValidDate;
            do {
                dob = JOptionPane.showInputDialog(null, "Enter Date of Birth (YYYY-MM-DD):");
                isValidDate = dob.matches("\\d{4}-\\d{2}-\\d{2}");
                if (!isValidDate) {
                    JOptionPane.showMessageDialog(null, "Date of Birth must be in the format YYYY-MM-DD!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!isValidDate);

            String phone;
            boolean validPhone;
            do {
                phone = JOptionPane.showInputDialog(null, "Enter Phone:");
                validPhone = (phone.matches("\\d{10,11}") && phone.startsWith("0"));
                if (!phone.matches("\\d{10,11}")) {
                    JOptionPane.showMessageDialog(null, "Phone number must be 10 or 11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!phone.startsWith("0")) {
                    JOptionPane.showMessageDialog(null, "Phone number must start by 0!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!validPhone);

            String idCard = JOptionPane.showInputDialog(null, "Enter ID Card:");
            String workingHours = "0";
            String salary1 = "0", salary2 = "0", salary3 = "0"; 
            String point = "0"; 

            // Role-specific fields
            if (role.equals("Employee")) {
                salary1 = JOptionPane.showInputDialog(null, "Enter Salary1:");
                salary2 = JOptionPane.showInputDialog(null, "Enter Salary2:");
                salary3 = JOptionPane.showInputDialog(null, "Enter Salary3:");
                workingHours = JOptionPane.showInputDialog(null, "Enter Working Hours:");
            } else if (role.equals("Customer")) {
                point = JOptionPane.showInputDialog(null, "Enter Point:");
            }

            boolean valid = false;
            while (!valid) {
                valid = true;

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() || dob.isEmpty() || phone.isEmpty() || idCard.isEmpty() || 
                    (role.equals("Employee") && (workingHours.isEmpty() || salary1.isEmpty() || salary2.isEmpty() || salary3.isEmpty())) || 
                    (role.equals("Customer") && point.isEmpty())) {
                    
                    JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    username = JOptionPane.showInputDialog(null, "Enter Username:", username);
                    password = JOptionPane.showInputDialog(null, "Enter Password:", password);
                    name = JOptionPane.showInputDialog(null, "Enter Name:", name);
                    role = (String) JOptionPane.showInputDialog(null, "Select Role:", "Role Selection", JOptionPane.QUESTION_MESSAGE, null, roles, role);
                    dob = JOptionPane.showInputDialog(null, "Enter Date of Birth (YYYY-MM-DD):", dob);
                    phone = JOptionPane.showInputDialog(null, "Enter Phone:", phone);
                    idCard = JOptionPane.showInputDialog(null, "Enter ID Card:", idCard);
                    if (role.equals("Employee")) {
                        workingHours = JOptionPane.showInputDialog(null, "Enter Working Hours:", workingHours);
                        salary1 = JOptionPane.showInputDialog(null, "Enter Salary1:", salary1);
                        salary2 = JOptionPane.showInputDialog(null, "Enter Salary2:", salary2);
                        salary3 = JOptionPane.showInputDialog(null, "Enter Salary3:", salary3);
                    }
                    if (role.equals("Customer")) {
                        point = JOptionPane.showInputDialog(null, "Enter Point:", point);
                    }
                    valid = false; 
                }

                String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                    checkStatement.setString(1, username);
                    ResultSet resultSet = checkStatement.executeQuery();
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "Username already exists. Please choose another one!", "Error", JOptionPane.ERROR_MESSAGE);
                            username = JOptionPane.showInputDialog(null, "Enter Username:", username);
                            valid = false; 
                        }
                    }
                }
            }

            String sqlInsert = "INSERT INTO users (id, username, password, name, role, dob, phone, id_card, working_hours, salary1, salary2, salary3, point) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                insertStatement.setString(1, id);
                insertStatement.setString(2, username);
                insertStatement.setString(3, password);
                insertStatement.setString(4, name);
                insertStatement.setString(5, role);
                insertStatement.setString(6, dob);
                insertStatement.setString(7, phone);
                insertStatement.setString(8, idCard);
                insertStatement.setString(9, role.equals("Employee") ? workingHours : null); 
                insertStatement.setString(10, role.equals("Employee") ? salary1 : null);
                insertStatement.setString(11, role.equals("Employee") ? salary2 : null);
                insertStatement.setString(12, role.equals("Employee") ? salary3 : null);
                insertStatement.setString(13, role.equals("Customer") ? point : null);
                insertStatement.executeUpdate();
            }

            tableModel.addRow(new Object[]{id, username, password, name, role, dob, phone, idCard, 
                (role.equals("Employee") ? workingHours : ""), 
                (role.equals("Employee") ? salary1 : ""), 
                (role.equals("Employee") ? salary2 : ""), 
                (role.equals("Employee") ? salary3 : ""), 
                (role.equals("Customer") ? point : "")});
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editUser(DefaultTableModel tableModel, JTable employeeTable) {
        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String password = (String) tableModel.getValueAt(selectedRow, 2);
            String name = (String) tableModel.getValueAt(selectedRow, 3);
            String role = (String) tableModel.getValueAt(selectedRow, 4);
            String dob = (String) tableModel.getValueAt(selectedRow, 5);
            String phone = (String) tableModel.getValueAt(selectedRow, 6);
            String idCard = (String) tableModel.getValueAt(selectedRow, 7);
            String workingHours = (String) tableModel.getValueAt(selectedRow, 8);
            String salary1 = (String) tableModel.getValueAt(selectedRow, 9);
            String point = (String) tableModel.getValueAt(selectedRow, 10);

            String salary2 = getSalary2FromDatabase(id); 
            String salary3 = getSalary3FromDatabase(id); 

            username = JOptionPane.showInputDialog(null, "Edit Username:", username);
            password = JOptionPane.showInputDialog(null, "Edit Password:", password);
            name = JOptionPane.showInputDialog(null, "Edit Name:", name);

            String[] roles = {"Manager", "Customer", "Employee"};
            role = (String) JOptionPane.showInputDialog(null, "Select Role:", "Role Selection", JOptionPane.QUESTION_MESSAGE, null, roles, role);
            boolean validDob;
            do {
                dob = JOptionPane.showInputDialog(null, "Edit Date of Birth (YYYY-MM-DD):", dob);
                validDob = dob.matches("\\d{4}-\\d{2}-\\d{2}");
                if (!validDob) {
                    JOptionPane.showMessageDialog(null, "Invalid date format! Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!validDob);
            boolean validPhone;
            do {
                phone = JOptionPane.showInputDialog(null, "Edit Phone:", phone);
                validPhone = phone.matches("0\\d{9,10}");
                if (!validPhone) {
                    JOptionPane.showMessageDialog(null, "Invalid phone number! It must start with 0 and have 10 or 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!validPhone);

            idCard = JOptionPane.showInputDialog(null, "Edit ID Card:", idCard);

            if (role.equals("Employee")) {
                salary1 = JOptionPane.showInputDialog(null, "Edit Salary1:", salary1);
                salary2 = JOptionPane.showInputDialog(null, "Edit Salary2:", salary2); 
                salary3 = JOptionPane.showInputDialog(null, "Edit Salary3:", salary3);
                workingHours = JOptionPane.showInputDialog(null, "Edit Working Hours:", workingHours);
                point = "";
            } else if (role.equals("Customer")) {
                point = JOptionPane.showInputDialog(null, "Edit Points:", point);
                salary1 = "";
                salary2 = "";
                salary3 = "";
                workingHours = "";
            } else { 
                salary1 = "";
                salary2 = "";
                salary3 = "";
                point = "";
                workingHours = "0";
            }

            tableModel.setValueAt(username, selectedRow, 1);
            tableModel.setValueAt(password, selectedRow, 2);
            tableModel.setValueAt(name, selectedRow, 3);
            tableModel.setValueAt(role, selectedRow, 4);
            tableModel.setValueAt(dob, selectedRow, 5);
            tableModel.setValueAt(phone, selectedRow, 6);
            tableModel.setValueAt(idCard, selectedRow, 7);
            tableModel.setValueAt(workingHours, selectedRow, 8);
            tableModel.setValueAt(salary1, selectedRow, 9); 
            tableModel.setValueAt(point, selectedRow, 10);

            updateUserInDatabase(id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3, point);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a valid user to edit.");
        }
    }

    private String getSalary2FromDatabase(String userId) {
        String salary2 = null;
        String query = "SELECT salary2 FROM users WHERE id = ?";

        try (Connection connection = new MySQLConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                salary2 = resultSet.getString("salary2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salary2;
    }
    private String getSalary3FromDatabase(String userId) {
        String salary3 = null;
        String query = "SELECT salary3 FROM users WHERE id = ?";

        try (Connection connection = new MySQLConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                salary3 = resultSet.getString("salary3");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salary3;
    }
	private void updateUserInDatabase(String id, String username, String password, String name, String role, String dob, String phone, String idCard, String workingHours, String salary1, String salary2, String salary3, String point) {
        String sqlUpdate = "UPDATE users SET username=?, password=?, name=?, role=?, dob=?, phone=?, id_card=?, working_hours=?, salary1=?, salary2=?, salary3=?, point=? WHERE id=?";
        try (Connection connection = new MySQLConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, role);
            statement.setString(5, dob);
            statement.setString(6, phone);
            statement.setString(7, idCard);
            statement.setString(8, workingHours);
            statement.setString(9, salary1);
            statement.setString(10, salary2);
            statement.setString(11, salary3);
            statement.setString(12, point);
            statement.setString(13, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
    
