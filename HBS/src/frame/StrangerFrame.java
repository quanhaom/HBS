package frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import services.Store;

public class StrangerFrame extends JFrame {

    public StrangerFrame(Store store, LoginFrame loginFrame) {

        setTitle("Stranger Frame");
        setSize(1200,800 );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new CustomerFrame(store, null,null,null).setVisible(true); 
        JOptionPane.showMessageDialog(this, "Welcome! You are in Stranger Mode.");
        dispose(); 
    }
}
