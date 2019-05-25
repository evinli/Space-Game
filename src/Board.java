import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private boolean gameOver;
    private int width, height;
    private boolean[] keys = new boolean[0xE3];

    private final int screenVel = 2;
    private final int shipSpeed = 6;
    private final int maxCooldown = 10;
    private int cooldown;
    private int counter;

    private Spaceship spaceShip;
    private ArrayList<Bullet> shots;
    private ArrayList<Bullet> enemyShots;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Enemy> enemies;
    private Background backOne, backTwo;



    public Board(int width, int height) {
        this.addKeyListener(this);
        this.setFocusable(true);

        this.width = width;
        this.height = height;
        gameOver = false;
        shots = new ArrayList<>();
        enemyShots = new ArrayList<>();

        spaceShip = new Spaceship(0, 0);
        obstacles = new ArrayList<>();
        enemies = new ArrayList<>();
        backOne = new Background(0);
        backTwo = new Background(-1 * backOne.getWidth());
        timer = new Timer(10, this);
        cooldown = 0;
        counter = 0;

        Obstacle testObstacle = new Obstacle("res/Planet.png", 1000, 500);
        obstacles.add(testObstacle);

        Enemy testEnemy = new Enemy("res/Earth.png", 1000, 100);
        enemies.add(testEnemy);
        obstacles.add(testEnemy);

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            //placeholder game over
            System.out.println("Game Over");
        }

        counter++;

        spaceShip.setX(spaceShip.getX() + screenVel);

        //Movement of the ship
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

        //update background
        backOne.update(width, getScreenOffset());
        backTwo.update(width, getScreenOffset());

        //Check if the ship should shoot
        if (keys[KeyEvent.VK_SPACE]) {
            if (cooldown <= 0) {
                cooldown = maxCooldown;
                shots.add(spaceShip.shoot());
            }
        }

        cooldown -= 1;

        if (counter % 20 == 0) {
            for (Enemy enemy : enemies) {
                //enemies only shoot if they are on the screen and if they are in front of the spaceshipa
                if (enemy.getX() - getScreenOffset() <= width && enemy.getX() > spaceShip.getX() + spaceShip.getWidth()) {
                    //shoot a shot aimed at the front tip of the spaceship
                    enemyShots.add(enemy.shoot(spaceShip.getX() + spaceShip.getWidth(),
                            spaceShip.getY() + spaceShip.getHeight() / 2));
                }
            }
        }

        //move each shot on the board
        ArrayList<Bullet> outOfBounds = new ArrayList<>();
        for (Bullet shot : shots) {
            shot.move();
            //remove the shot if it moves out of bounds
            if (shot.getX() - getScreenOffset() > width) {
                outOfBounds.add(shot);
            }
        }
        shots.removeAll(outOfBounds);

        outOfBounds.clear();
        for (Bullet shot : enemyShots) {
            shot.move();
            //remove the shot if it moves out of bounds
            if (shot.getX() - getScreenOffset() > width || shot.getX() - getScreenOffset() < 0) {
                outOfBounds.add(shot);
            }
        }
        enemyShots.removeAll(outOfBounds);

        checkCollisions();

        this.repaint();
    }

    public void checkCollisions() {
        Rectangle shipHitBox = spaceShip.getBounds();
        for (Obstacle obstacle : obstacles) {
            if (shipHitBox.intersects(obstacle.getBounds())) {
                gameOver = true;
            }
        }

        ArrayList<Obstacle> toDelete = new ArrayList<>();
        for (Bullet shot : shots) {
            Rectangle shotHitBox = shot.getBounds();

            for (Obstacle obstacle : obstacles) {
                if (shotHitBox.intersects(obstacle.getBounds())) {
                    toDelete.add(obstacle);
                }
            }
        }
        enemies.removeAll(toDelete);
        obstacles.removeAll(toDelete);
    }
    
    private int getScreenOffset() {
        return spaceShip.getX() - 300;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        backOne.draw(g2d, this, getScreenOffset());
        backTwo.draw(g2d, this, getScreenOffset());

        spaceShip.draw(g2d, this, getScreenOffset());

        for (Bullet shot : shots) {
            shot.draw(g2d, this, getScreenOffset());
        }

        for (Bullet shot : enemyShots) {
            shot.draw(g2d, this, getScreenOffset());
        }

        for (Obstacle o : obstacles) {
            o.draw(g2d, this, getScreenOffset());
        }

        /*for (Enemy e : enemies) {
            e.draw(g2d, this, getScreenOffset());
        }*/



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