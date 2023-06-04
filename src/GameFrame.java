import javax.swing.*;

public class GameFrame {
    public GameFrame(){
        JFrame frame = new JFrame("FIB Carros");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.setVisible(true);
    }
}
