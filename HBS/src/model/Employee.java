package model;

public class Employee extends Person {
    private double salary1;
    private double salary2;
    private double salary3;
    private String dob;
    private String phone;
    private String idCard;
    private int workingHours;

    public Employee(String id, String username, String password,String name, String role, String dob, String phone, String idCard, int workingHours,double salary1,double salary2,double salary3) {
        super(id, username, password,name, role);
        this.dob = dob;
        this.phone = phone;
        this.idCard = idCard;
        this.workingHours = workingHours;
        this.salary1 = salary1;
        
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
    public double getSalary1() {
    	return salary1;
    }
    public double getSalary2() {
    	return salary2;
    }
    public double getSalary3() {
    	return salary3;
    }

    @Override
    public String toString() {
        return "Employee [ID=" + getId() + ", Name=" + getName() + ", Role=" + getRole() + 
               ", DOB=" + dob + ", Phone=" + phone + 
               ", ID Card=" + idCard + ", Working Hours=" + workingHours + "]";
    }
}
