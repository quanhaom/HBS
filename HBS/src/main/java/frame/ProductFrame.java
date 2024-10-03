package frame;


import javax.swing.JFrame;


import services.Store;

public class ProductFrame extends EmployeeFrame {
    private ManagerFrame managerFrame;

    public ProductFrame(Store store, ManagerFrame managerFrame) {
        super(store,null);  // Call BaseFrame constructor
        this.managerFrame = managerFrame;
        setTitle("Product Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }
}
