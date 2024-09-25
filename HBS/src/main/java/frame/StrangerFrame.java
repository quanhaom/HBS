package frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import services.Store;

public class StrangerFrame extends JFrame {
    private Store store;

    public StrangerFrame(Store store) {
        this.store = store;

        setTitle("Stranger Frame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Open CustomerFrame directly
        new CustomerFrame(store, null).setVisible(true); // Pass null for LoginFrame reference
        JOptionPane.showMessageDialog(this, "Welcome! You are in Stranger Mode.");
        dispose(); // Close the StrangerFrame
    }
}
