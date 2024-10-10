package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Manager;
import services.MySQLConnection;

import java.awt.*;
import java.sql.*;

public class EmpmanaFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, removeButton, backButton;
    private JComboBox<String> salaryOptions;
    private ManagerFrame managerFrame;
    Manager manager = new Manager();

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
        addButton.addActionListener(e -> manager.addUser(tableModel));

        editButton = new JButton("Edit User");
        actionPanel.add(editButton);
        editButton.addActionListener(e -> manager.editUser(tableModel,employeeTable));

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
        try (Connection connection = new MySQLConnection().getConnection();
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

                String[] row = new String[11];
                row[0] = id != null ? id : "";
                row[1] = username != null ? username : ""; 
                row[2] = password != null ? password : ""; 
                row[3] = name != null ? name : "";
                row[4] = role != null ? role : "";
                row[5] = dob != null ? dob : "";
                row[6] = phone != null ? phone : ""; 
                row[7] = idCard != null ? idCard : "";

                if (role.equals("Customer")) {
                    row[8] = "";
                    row[9] = "";
                    row[10] = point != null ? point : "aaa"; 
                } else if (role.equals("Employee")) {
                    row[8] = workingHours != null ? workingHours : "";
                    row[9] = salary1 != null ? salary1 : "";
                    row[10] =  "";
                }

                tableModel.addRow(row);
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
            try (Connection connection = new MySQLConnection().getConnection();
                 Statement statement = connection.createStatement()) {
                
                String query = "SELECT salary1, salary2, salary3 FROM users WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    if (resultSet.next()) {
                        String salary = switch (selectedOption) {
                            case 0 -> resultSet.getString("salary1");
                            case 1 -> resultSet.getString("salary2");
                            case 2 -> resultSet.getString("salary3");
                            default -> "";
                        };
                        tableModel.setValueAt(salary, i, 9);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        try (Connection connection = new MySQLConnection().getConnection();
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
