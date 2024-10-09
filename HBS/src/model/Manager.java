package model;

import services.DatabaseConnection;

public class Manager extends Employee {

    public Manager(String id, String username, String password, String name, String role,
                   String dob, String phone, String idCard, int workingHours,
                   double salary1, double salary2, double salary3,
                   DatabaseConnection dbConnection) {
        super(id, username, password, name, role, dob, phone, idCard, workingHours, salary1, salary2, salary3, dbConnection);
    }



    public double calculateTotalCompensation() {
        return getSalary1() + getSalary2() + getSalary3() ; 
    }

    @Override
    public String toString() {
        return "Manager [ID=" + getId() + ", Name=" + getName() + ", Role=" + getRole() + 
               ", DOB=" + getDob() + ", Phone=" + getPhone() + 
               ", ID Card=" + getIdCard() + ", Working Hours=" + getWorkingHours() 
                + "]";
    }
}
