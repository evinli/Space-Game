import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener {
    private Spaceship spaceShip;
    private Timer timer;
    private boolean gameOver;

    public Board() {
        spaceShip = new Spaceship("res/thing_test.png", 0, 0);
        this.addKeyListener(this);
        this.setFocusable(true);
        gameOver = false;
        timer = new Timer(10, this);
        timer.start();
    }

        private void draw() {}

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(),
                spaceShip.getY(), this);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_UP):
                spaceShip.setY(spaceShip.getY() - 5);
                break;
            case (KeyEvent.VK_RIGHT):
                System.out.println("Right");
                spaceShip.setX(spaceShip.getX() + 5);
                break;
            case (KeyEvent.VK_DOWN):
                spaceShip.setY(spaceShip.getY() + 5);
                break;
            case (KeyEvent.VK_LEFT):
                spaceShip.setX(spaceShip.getX() - 5);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}