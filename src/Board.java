import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener, KeyListener, MouseListener {
    private Timer timer;
    private boolean gameOver;
    private int width, height;
    private boolean[] keys = new boolean[0xE3];

    private final int screenVel = 0;
    private final int shipSpeed = 6;
    private final int maxCooldown = 20;
    private int cooldown, counter, obstacleMarker = 0, enemyMarker = 0;

    private Spaceship spaceShip;
    private ArrayList<Bullet> shots;
    private ArrayList<Bullet> enemyShots;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Enemy> enemies;
    private Background backOne, backTwo;



    public Board(int width, int height) {
        this.addKeyListener(this);
        this.setFocusable(true);

        addMouseListener(this);

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

        spawnObstacles();

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

        //spawn obstacles
        if (getScreenOffset() > obstacleMarker + 750) {
            obstacleMarker = getScreenOffset();
            for (int i = 0; i < 3; i++) {
                //one in 5 chance to spawn an enemy instead of an obstacle
                if (Math.random() < .2) {
                    spawnEnemy();
                } else {
                    spawnObstacles();
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
        checkOutofBounds();

        this.repaint();
    }

    private void checkCollisions() {
        Rectangle shipHitBox = spaceShip.getBounds();
        for (Obstacle obstacle : obstacles) {
            if (shipHitBox.intersects(obstacle.getBounds())) {
                gameOver = true;
            }
        }

        for (Bullet b : enemyShots) {
            if (shipHitBox.intersects(b.getBounds())) {
                gameOver = true;
            }
        }

        ArrayList<Enemy> toDelete = new ArrayList<>();
        for (Bullet shot : shots) {
            Rectangle shotHitBox = shot.getBounds();

            for (Enemy e: enemies) {
                if (shotHitBox.intersects(e.getBounds())) {
                    toDelete.add(e);
                }
            }
        }
        enemies.removeAll(toDelete);
    }

    private void checkOutofBounds() {
        ArrayList<Obstacle> outOfBounds = new ArrayList<>();

        for (Obstacle o : obstacles) {
            if (o.getX() - getScreenOffset() + o.getWidth() < 0) {
                outOfBounds.add(o);
            }
        }
        obstacles.removeAll(outOfBounds);
    }
    
    private int getScreenOffset() {
        return spaceShip.getX() - 300;
    }

    private void spawnObstacles() {
        String[] sprites = {"res/Planet.png", "res/Earth.png"};
        Random r=new Random();

        Obstacle obstacle = new Obstacle(sprites[r.nextInt(sprites.length)], getScreenOffset() + width +
                (int)(Math.random() * 500), (int)(Math.random() * (height - 200)));

        checkOverlap(obstacle);
        obstacles.add(obstacle);
    }

    private void spawnEnemy() {
        Enemy enemy = new Enemy("res/Earth.png", getScreenOffset() + width + (int)(Math.random() * 500),
                (int)(Math.random() * (height - 200)));

        checkOverlap(enemy);
        enemies.add(enemy);
    }

    private void checkOverlap(Obstacle obstacle) {
        //reposition the obstacle if it overlaps other obstacles
        boolean overlap;
        do {
            overlap = false;
            for (Obstacle o : obstacles) {
                if (obstacle.getBounds().intersects(o.getBounds())) {
                    obstacle.setX(getScreenOffset() + width + (int)(Math.random() * 500));
                    obstacle.setY((int)(Math.random() * (height - 200)));
                    overlap = true;
                }
            }
            for (Enemy e : enemies) {
                if (obstacle.getBounds().intersects(e.getBounds())) {
                    obstacle.setX(getScreenOffset() + width + (int)(Math.random() * 500));
                    obstacle.setY((int)(Math.random() * (height - 200)));
                    overlap = true;
                }
            }
        } while (overlap);
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

        for (Enemy e : enemies) {
            e.draw(g2d, this, getScreenOffset());
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

    @Override
    public void mouseClicked(MouseEvent event) {
        System.out.println("asdfasdf");
        int x = event.getX();
        int y = event.getY();

        obstacles.add(new Obstacle("res/Planet.png", x + getScreenOffset(), y));
    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) { }

    @Override
    public void mouseEntered(MouseEvent event) { }

    @Override
    public void mouseExited(MouseEvent event) { }

}