package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpmanaFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel; // Mô hình cho bảng
    private JButton addButton, editButton, removeButton;
    private JTextField searchField;
    private JComboBox<String> sortOptions;
    private JComboBox<String> salaryOptions;

    public EmpmanaFrame() {
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Password", "Name", "Role", "DOB", "Phone", "ID Card", "Working Hours", "Salary1", "Salary2", "Salary3", "Point"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.SOUTH);

        searchField = new JTextField(15);
        actionPanel.add(searchField);

        addButton = new JButton("Add User");
        actionPanel.add(addButton);
        addButton.addActionListener(e -> addUser());

        editButton = new JButton("Edit User");
        actionPanel.add(editButton);
        editButton.addActionListener(e -> editUser());

        removeButton = new JButton("Remove User");
        actionPanel.add(removeButton);
        removeButton.addActionListener(e -> removeUser());

        // Thêm tùy chọn sắp xếp
        sortOptions = new JComboBox<>(new String[]{"Sort by Name", "Sort by ID"});
        sortOptions.addActionListener(e -> updateSortedUserDisplay());
        actionPanel.add(sortOptions);

        // Thêm tùy chọn hiển thị lương
        salaryOptions = new JComboBox<>(new String[]{"Show Salary1", "Show Salary2", "Show Salary3"});
        salaryOptions.addActionListener(e -> updateSalaryDisplay());
        actionPanel.add(salaryOptions);

        loadUsersFromDatabase();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadUsersFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hbs_db", "root", "iuhuyenlemleM0@");
            Statement statement = connection.createStatement();
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
                String salary2 = resultSet.getString("salary2");
                String salary3 = resultSet.getString("salary3");
                String point = resultSet.getString("point");

                tableModel.addRow(new Object[]{id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3, point});
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.");
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

            String dob = JOptionPane.showInputDialog(this, "Enter Date of Birth (YYYY-MM-DD):");
            String phone = JOptionPane.showInputDialog(this, "Enter Phone:");
            String idCard = JOptionPane.showInputDialog(this, "Enter ID Card:");
            String workingHours = JOptionPane.showInputDialog(this, "Enter Working Hours:");
            String salary1 = JOptionPane.showInputDialog(this, "Enter Salary1:");
            String salary2 = JOptionPane.showInputDialog(this, "Enter Salary2:");
            String salary3 = JOptionPane.showInputDialog(this, "Enter Salary3:");
            String point = JOptionPane.showInputDialog(this, "Enter Point:");

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
                insertStatement.setString(9, workingHours);
                insertStatement.setString(10, salary1);
                insertStatement.setString(11, salary2);
                insertStatement.setString(12, salary3);
                insertStatement.setString(13, point);
                insertStatement.executeUpdate();
            }

            tableModel.addRow(new Object[]{id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3, point});
        } catch (SQLException e) {
            e.printStackTrace();
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
            String salary1 = (String) tableModel.getValueAt(selectedRow, 9);
            String salary2 = (String) tableModel.getValueAt(selectedRow, 10);
            String salary3 = (String) tableModel.getValueAt(selectedRow, 11);
            String point = (String) tableModel.getValueAt(selectedRow, 12);

            username = JOptionPane.showInputDialog(this, "Edit Username:", username);
            password = JOptionPane.showInputDialog(this, "Edit Password:", password);
            name = JOptionPane.showInputDialog(this, "Edit Name:", name);
            
            String[] roles = {"Manager", "Customer", "Employee"};
            role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role Selection",
                    JOptionPane.QUESTION_MESSAGE, null, roles, role); // Default to current role

            dob = JOptionPane.showInputDialog(this, "Edit Date of Birth (YYYY-MM-DD):", dob);
            phone = JOptionPane.showInputDialog(this, "Edit Phone:", phone);
            idCard = JOptionPane.showInputDialog(this, "Edit ID Card:", idCard);
            workingHours = JOptionPane.showInputDialog(this, "Edit Working Hours:", workingHours);
            salary1 = JOptionPane.showInputDialog(this, "Edit Salary1:", salary1);
            salary2 = JOptionPane.showInputDialog(this, "Edit Salary2:", salary2);
            salary3 = JOptionPane.showInputDialog(this, "Edit Salary3:", salary3);
            point = JOptionPane.showInputDialog(this, "Edit Point:", point);

            tableModel.setValueAt(username, selectedRow, 1);
            tableModel.setValueAt(password, selectedRow, 2);
            tableModel.setValueAt(name, selectedRow, 3);
            tableModel.setValueAt(role, selectedRow, 4);
            tableModel.setValueAt(dob, selectedRow, 5);
            tableModel.setValueAt(phone, selectedRow, 6);
            tableModel.setValueAt(idCard, selectedRow, 7);
            tableModel.setValueAt(workingHours, selectedRow, 8);
            tableModel.setValueAt(salary1, selectedRow, 9);
            tableModel.setValueAt(salary2, selectedRow, 10);
            tableModel.setValueAt(salary3, selectedRow, 11);
            tableModel.setValueAt(point, selectedRow, 12);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void removeUser() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this user?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
        }
    }




    private void updateSortedUserDisplay() {
        JOptionPane.showMessageDialog(this, "Sorting functionality not implemented yet.");
    }

    private void updateSalaryDisplay() {
        JOptionPane.showMessageDialog(this, "Salary display functionality not implemented yet.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpmanaFrame::new);
    }
}
