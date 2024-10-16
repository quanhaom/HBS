package model;

public class Employee extends Person {
    private double salary;
    private String dob;
    private String phone;
    private String idCard;
    private int workingHours;

    public Employee(String id, String name, String password, String role, String dob, String phone, String idCard, int workingHours) {
        super(id, name, password, role);
        this.dob = dob;
        this.phone = phone;
        this.idCard = idCard;
        this.workingHours = workingHours;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String toString() {
        return "Employee [ID=" + getId() + ", Name=" + getName() + ", Role=" + getRole() + 
               ", DOB=" + dob + ", Phone=" + phone + 
               ", ID Card=" + idCard + ", Working Hours=" + workingHours + "]";
    }
}
