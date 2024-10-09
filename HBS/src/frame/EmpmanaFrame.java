package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmpmanaFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, removeButton, backButton;
    private JComboBox<String> salaryOptions;
    private ManagerFrame managerFrame;

    public EmpmanaFrame(ManagerFrame managerFrame) {
        this.managerFrame = managerFrame;

        setTitle("User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Password", "Name", "Role", "DOB", "Phone", "ID Card", "Working Hours", "Salary", "Point"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.SOUTH);

        addButton = new JButton("Add User");
        actionPanel.add(addButton);
        addButton.addActionListener(e -> addUser());

        editButton = new JButton("Edit User");
        actionPanel.add(editButton);
        editButton.addActionListener(e -> editUser());

        removeButton = new JButton("Remove User");
        actionPanel.add(removeButton);
        removeButton.addActionListener(e -> removeUser());

        backButton = new JButton("Back");
        actionPanel.add(backButton);
        backButton.addActionListener(e -> back());

        salaryOptions = new JComboBox<>(new String[]{"Show January salary", "Show February salary", "Show March salary"});
        salaryOptions.addActionListener(e -> updateSalaryDisplay());
        actionPanel.add(salaryOptions);

        loadUsersFromDatabase();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadUsersFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                String dob = resultSet.getString("dob");
                String phone = resultSet.getString("phone");
                String idCard = resultSet.getString("id_card");
                String workingHours = resultSet.getString("working_hours");
                String salary1 = resultSet.getString("salary1");
                String point = resultSet.getString("point");

                tableModel.addRow(new Object[]{id, username, password, name, role, dob, phone, idCard, workingHours, salary1, point});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.");
        }
    }

    private void updateSalaryDisplay() {
        int selectedOption = salaryOptions.getSelectedIndex();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
                 Statement statement = connection.createStatement()) {
                String query = "SELECT salary1, salary2, salary3 FROM users WHERE id = " + id;
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    String salary = switch (selectedOption) {
                        case 0 -> resultSet.getString("salary1");
                        case 1 -> resultSet.getString("salary2");
                        case 2 -> resultSet.getString("salary3");
                        default -> "";
                    };
                    tableModel.setValueAt(salary, i, 9);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addUser() {
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

            String username = JOptionPane.showInputDialog(this, "Enter Username:");
            String password = JOptionPane.showInputDialog(this, "Enter Password:");
            String name = JOptionPane.showInputDialog(this, "Enter Name:");

            String[] roles = {"Manager", "Customer", "Employee"};
            String role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role Selection",
                    JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

            String dob;
            boolean isValidDate;
            do {
                dob = JOptionPane.showInputDialog(this, "Enter Date of Birth (YYYY-MM-DD):");
                isValidDate = dob.matches("\\d{4}-\\d{2}-\\d{2}");
                if (!isValidDate) {
                    JOptionPane.showMessageDialog(this, "Date of Birth must be in the format YYYY-MM-DD!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!isValidDate);

            String phone;
            boolean validPhone;
            do {
                phone = JOptionPane.showInputDialog(this, "Enter Phone:");
                validPhone = (phone.matches("\\d{10,11}") && phone.startsWith("0")); 
                if (!phone.matches("\\d{10,11}")) {
                    JOptionPane.showMessageDialog(this, "Phone number must be 10 or  11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (!phone.startsWith("0")) {
                	JOptionPane.showMessageDialog(this, "Phone number must start by 0!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!validPhone);

            String idCard = JOptionPane.showInputDialog(this, "Enter ID Card:");
            String workingHours = "0";
            String salary1 = "0", salary2 = "0", salary3 = "0"; 
            
            if (!role.equals("Manager")) {
                salary1 = JOptionPane.showInputDialog(this, "Enter Salary1:");
                salary2 = JOptionPane.showInputDialog(this, "Enter Salary2:");
                salary3 = JOptionPane.showInputDialog(this, "Enter Salary3:");
            }

            String point = "0"; 

            if (role.equals("Customer")){
                point = JOptionPane.showInputDialog(this, "Enter Point:");
            }

            boolean valid = false;
            while (!valid) {
                valid = true;

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() || dob.isEmpty() || phone.isEmpty() || idCard.isEmpty() || 
                    (role.equals("Manager") ? workingHours.isEmpty() : false) || 
                    (role.equals("Manager") ? (salary1.isEmpty() || salary2.isEmpty() || salary3.isEmpty()) : false) || 
                    (role.equals("Customer") || role.equals("Employee") ? point.isEmpty() : false)) {
                    
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    username = JOptionPane.showInputDialog(this, "Enter Username:", username);
                    password = JOptionPane.showInputDialog(this, "Enter Password:", password);
                    name = JOptionPane.showInputDialog(this, "Enter Name:", name);
                    role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role Selection", JOptionPane.QUESTION_MESSAGE, null, roles, role);
                    dob = JOptionPane.showInputDialog(this, "Enter Date of Birth (YYYY-MM-DD):", dob);
                    phone = JOptionPane.showInputDialog(this, "Enter Phone:", phone);
                    idCard = JOptionPane.showInputDialog(this, "Enter ID Card:", idCard);
                    if (role.equals("Manager")) {
                        workingHours = JOptionPane.showInputDialog(this, "Enter Working Hours:", workingHours);
                        salary1 = JOptionPane.showInputDialog(this, "Enter Salary1:", salary1);
                        salary2 = JOptionPane.showInputDialog(this, "Enter Salary2:", salary2);
                        salary3 = JOptionPane.showInputDialog(this, "Enter Salary3:", salary3);
                    }
                    if (role.equals("Customer") || role.equals("Employee")) {
                        point = JOptionPane.showInputDialog(this, "Enter Point:", point);
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
                            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one!", "Error", JOptionPane.ERROR_MESSAGE);
                            username = JOptionPane.showInputDialog(this, "Enter Username:", username);
                            valid = false; 
                        }
                    }
                }
            }

            String sqlInsert = "INSERT INTO users (id, username, password, name, role, dob, phone, id_card, working_hours, salary1, salary2, salary3, point) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            boolean insertSuccessful = false;
            while (!insertSuccessful) {
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, id);
                    insertStatement.setString(2, username);
                    insertStatement.setString(3, password);
                    insertStatement.setString(4, name);
                    insertStatement.setString(5, role);
                    insertStatement.setString(6, dob);
                    insertStatement.setString(7, phone);
                    insertStatement.setString(8, idCard);
                    insertStatement.setString(9, role.equals("Manager") ? workingHours : null); // Set working hours only for managers
                    insertStatement.setString(10, salary1);
                    insertStatement.setString(11, salary2);
                    insertStatement.setString(12, salary3);
                    insertStatement.setString(13, point);
                    insertStatement.executeUpdate();
                    insertSuccessful = true; 
                } catch (SQLException e) {
                    if (e.getMessage().contains("Data truncation: Incorrect date value")) {
                        JOptionPane.showMessageDialog(this, "Invalid date of birth! Please enter a valid date (YYYY-MM-DD):", "Error", JOptionPane.ERROR_MESSAGE);
                        dob = JOptionPane.showInputDialog(this, "Enter Date of Birth (YYYY-MM-DD):", dob);
                    } else {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "An error occurred while adding the user!", "Error", JOptionPane.ERROR_MESSAGE);
                        break; 
                    }
                }
            }

            tableModel.addRow(new Object[]{id, username, password, name, role, dob, phone, idCard, 
                (role.equals("Employee") ? workingHours : ""), 
                (role.equals("Employee") ? "" : salary1), 
                (role.equals("Employee") ? "" : salary2), 
                (role.equals("Employee") ? "" : salary3), 
                (role.equals("Customer") ? point : "")});
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while adding the user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editUser() {
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
            String point = (String) tableModel.getValueAt(selectedRow, 9);
            String salary = (String) tableModel.getValueAt(selectedRow, 10);

            username = JOptionPane.showInputDialog(this, "Edit Username:", username);
            password = JOptionPane.showInputDialog(this, "Edit Password:", password);
            name = JOptionPane.showInputDialog(this, "Edit Name:", name);

            String[] roles = {"Manager", "Customer", "Employee"};
            role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role Selection",
                    JOptionPane.QUESTION_MESSAGE, null, roles, role);

            dob = JOptionPane.showInputDialog(this, "Edit Date of Birth (YYYY-MM-DD):", dob);
            phone = JOptionPane.showInputDialog(this, "Edit Phone:", phone);
            idCard = JOptionPane.showInputDialog(this, "Edit ID Card:", idCard);

            String salary1 = "";
            String salary2 = "";
            String salary3 = "";

            if (role.equals("Employee")) {
                salary1 = JOptionPane.showInputDialog(this, "Edit Salary1:", salary);
                salary2 = JOptionPane.showInputDialog(this, "Edit Salary2:", "");
                salary3 = JOptionPane.showInputDialog(this, "Edit Salary3:", ""); 
                workingHours = JOptionPane.showInputDialog(this, "Edit Working Hours:", "");
                point = "0";
            } else if (role.equals("Customer")) {
                point = JOptionPane.showInputDialog(this, "Edit Points:", point);
                salary1 = "0"; 
                salary2 = "0";
                salary3 = "0";
                workingHours = "0";
            } else {
                salary1 = "0";
                salary2 = "0";
                salary3 = "0";
                point = "0"; 
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
            tableModel.setValueAt(salary3, selectedRow, 9); 
            tableModel.setValueAt(point, selectedRow, 10); 

            updateUserInDatabase(id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3, point);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid user to edit.");
        }
    }

    private void updateUserInDatabase(String id, String username, String password, String name, String role, String dob, String phone, String idCard, String workingHours, String salary1, String salary2, String salary3, String point) {
        String sqlUpdate = "UPDATE users SET username=?, password=?, name=?, role=?, dob=?, phone=?, id_card=?, working_hours=?, salary1=?, salary2=?, salary3=?, point=? WHERE id=?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
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


    private void removeUser() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this user?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                removeUserFromDatabase(id);
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
        }
    }

    private void removeUserFromDatabase(String id) {
        String sqlDelete = "DELETE FROM users WHERE id=?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
             PreparedStatement statement = connection.prepareStatement(sqlDelete)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void back() {
        setVisible(false);
        managerFrame.setVisible(true);
    }
}
