import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.swing.*;

public class Main {
    public static String id;

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter your ID:");
        String clue = "https://app.seker.live/fm1/solution/getBoard?id=" + id;

        if (input != null) {
            id = input;
            SwingUtilities.invokeLater(() -> {
                new Window().setVisible(true);
            });
        } else {
            System.out.println("GoodBye!");
        }
    }
}
