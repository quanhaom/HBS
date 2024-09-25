package model;

public class Employee extends Person {
    private double salary; // Additional attribute specific to Employee

    // Constructor
    public Employee(String id, String name, String password, String role, double salary) {
        super(id, name, password, role); // Call the constructor of the Person class
        this.salary = salary; // Initialize salary
    }

    // Getter for salary
    public double getSalary() {
        return salary;
    }

    // Setter for salary
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee [ID=" + getId() + ", Name=" + getName() + ", Role=" + getRole() + ", Salary=" + salary + "]";
    }
}
