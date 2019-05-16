import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private boolean gameOver;
    private int width, height;
    private boolean[] keys = new boolean[0xE3];

    private final int shipSpeed = 3;
    private final int maxCooldown = 10;
    private int cooldown;

    private Spaceship spaceShip;
    private ArrayList<Bullet> shots;



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
        cooldown = 0;
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

        if (keys[KeyEvent.VK_W]) {
            spaceShip.setY(spaceShip.getY() - shipSpeed);
        }
        if (keys[KeyEvent.VK_S]) {
            spaceShip.setY(spaceShip.getY() + shipSpeed);
        }
        if (keys[KeyEvent.VK_A]) {
            spaceShip.setX(spaceShip.getX() - shipSpeed);
        }
        if (keys[KeyEvent.VK_D]) {
            spaceShip.setX(spaceShip.getX() + shipSpeed);
        }

        if (keys[KeyEvent.VK_SPACE]) {
            if (cooldown <= 0) {
                cooldown = maxCooldown;
                shoot();
            }
        }
        cooldown -=  1;
        

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
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}