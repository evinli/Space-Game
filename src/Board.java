import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener {
    private Spaceship spaceShip;
    private Timer timer;
    private boolean gameOver;
    private ArrayList<Bullet> shots;
    private int width, height;


    public Board(int width, int height) {
        spaceShip = new Spaceship(0, 0);

        this.addKeyListener(this);
        this.setFocusable(true);

        this.width = width;
        this.height = height;
        gameOver = false;
        shots = new ArrayList<>();

        spaceShip = new Spaceship(0, 0);
        timer = new Timer(10, this);
        timer.start();
        shoot();
    }

    public void shoot() {
        Bullet shot = new Bullet("res/bullet_test.png", spaceShip.getX(), spaceShip.getY());
        shots.add(shot);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //move each shot on the board
        ArrayList<Bullet> outOfBounds = new ArrayList<>();
        for (Bullet shot : shots) {
            shot.move();
            //remove the shot if it moves out of bounds
            if (shot.getX() > width || shot.getX() < 0 - shot.getWidth()) {
                outOfBounds.add(shot);
            }
        }
        shots.removeAll(outOfBounds);

        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(),
                spaceShip.getY(), this);

        for (Bullet shot : shots) {
            g2d.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }

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