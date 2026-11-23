package wildwestoutlaw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    public static JFrame window;
    public static int screenWidth;
    public static int screenHeight;

    public Main () {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        Main.window.setSize(screenWidth, screenHeight);
    }
    
    public static void main (String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setTitle("Wild West Outlaw");
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBackground(Color.decode("#eeca84"));
        window.add(gamePanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}