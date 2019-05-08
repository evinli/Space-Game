import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {
    private Sprite spaceShip;


    public Board() {
        spaceShip = new Sprite("res/thing_test.png");
        spaceShip.setX(10);
    }

        private void draw() {};

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(),
                spaceShip.getY(), this);

        Toolkit.getDefaultToolkit().sync();
    }
}