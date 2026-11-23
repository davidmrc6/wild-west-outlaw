package wildwestoutlaw;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main (String[] args) {

        // Swing windows have to be created on Swing's own thread
        SwingUtilities.invokeLater(() -> {

            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setTitle("Wild West Outlaw");

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setVisible(true);
            gamePanel.requestFocusInWindow();

            gamePanel.startGameThread();
        });
    }
}
