package frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import services.Store;

public class StrangerFrame extends JFrame {
    private Store store;
    private LoginFrame loginFrame; 

    public StrangerFrame(Store store, LoginFrame loginFrame) {
    	this.loginFrame = loginFrame;
        this.store = store;

        setTitle("Stranger Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new CustomerFrame(store, null).setVisible(true); 
        JOptionPane.showMessageDialog(this, "Welcome! You are in Stranger Mode.");
        dispose(); 
    }
}
