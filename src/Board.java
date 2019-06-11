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

    private final int SCREENVEL = 2;
    private final int SHIPSPEED = 6;
    private final int MAXCOOLDOWN = 20;
    private int cooldown, obstacleMarker, enemyCounter;

    private Spaceship spaceShip;
    private ArrayList<Bullet> shots;
    private ArrayList<Bullet> enemyShots;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Enemy> enemies;
    private ArrayList<MovingEnemy> mEnemies;
    private Background backOne, backTwo;
    private boolean shipDirection;



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
        mEnemies = new ArrayList<>();
        backOne = new Background(0);
        backTwo = new Background(-1 * backOne.getWidth());
        timer = new Timer(10, this);
        cooldown = 0;
        obstacleMarker = 0;
        enemyCounter = 0;

        //spawns one obstacle to begin with
        spawnObstacles();

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (gameOver) {
            //placeholder game over
            System.out.println("Game Over");
        }
        if (!gameOver) {
            moveShip();

            //update background
            backOne.update(width, getScreenOffset());
            backTwo.update(width, getScreenOffset());

            //Check if the ship should shoot
            if (keys[KeyEvent.VK_SPACE]) {
                if ((cooldown <= 0) && (shipDirection)) {
                    cooldown = MAXCOOLDOWN;
                    shots.add(spaceShip.shoot(shipDirection));
                }
            }

            cooldown -= 1;

            //Check if enemies should shoot
            for (Enemy enemy : enemies) {
                //enemies only shoot if they are on the screen and if they are in front of the spaceship
                if (enemy.shouldShoot(getScreenOffset(), width, spaceShip)) {
                    //shoot a shot aimed at the front tip of the spaceship
                    enemyShots.add(enemy.shoot(spaceShip.getX() + spaceShip.getWidth(),
                            spaceShip.getY() + spaceShip.getHeight() / 2));
                }
            }
            for (MovingEnemy enemy : mEnemies) {
                //enemies only shoot if they are on the screen and if they are in front of the spaceship
                if (enemy.shouldShoot(getScreenOffset(), width, spaceShip)) {
                    //shoot a shot aimed at the front tip of the spaceship
                    enemyShots.add(enemy.shoot(spaceShip.getX() + spaceShip.getWidth(),
                            spaceShip.getY() + spaceShip.getHeight() / 2));
                }
            }

            //spawn obstacles once every time the screen pans 750 to the right
            if (getScreenOffset() > obstacleMarker + 750) {
                obstacleMarker = getScreenOffset();
                if (Math.random() < .1) {//one in 10 chance to spawn moving obstacle
                    spawnMovingObstacle();
                } else {//otherwise spawn a mix of 5 obstacles/enemies
                    for (int i = 0; i < 3; i++) {
                        //one in 5 chance to spawn an enemy instead of an obstacle
                        if (Math.random() < .2) {
                            spawnEnemy();
                        } else {
                            spawnObstacles();
                        }
                    }
                }
            }

            //Move the moving obstacles
            for (MovingEnemy e : mEnemies) {
                if (e.getY() < 0) {
                    e.changeDirection();
                } else if (e.getY() > height - e.getHeight()) {
                    e.changeDirection();
                }
                e.move();
            }

            //Move each shot on the board
            moveShots(shots);
            moveShots(enemyShots);

            //check if the ship has collided with anything
            checkShipCollisions();

            //remove all obstacles, enemies, and moving enemies that are out of bounds
            checkOutofBounds();
        }

        this.repaint();
    }

    public void moveShip() {
        spaceShip.setX(spaceShip.getX() + SCREENVEL);

        spaceShip.loadImage("res/Spaceship.png");
        shipDirection = true;

        //Movement of the ship
        if (keys[KeyEvent.VK_W] && spaceShip.getY() > 0) {
            spaceShip.setY(spaceShip.getY() - SHIPSPEED);
        }

        if (keys[KeyEvent.VK_S] && spaceShip.getY() < height - spaceShip.getHeight()) {
            spaceShip.setY(spaceShip.getY() + SHIPSPEED);
        }

        if (keys[KeyEvent.VK_A]) {
            spaceShip.setX(spaceShip.getX() - SHIPSPEED);
            spaceShip.loadImage("res/SpaceshipLeft.png");
            shipDirection = false;
        }
        if (keys[KeyEvent.VK_D]) {
            spaceShip.setX(spaceShip.getX() + SHIPSPEED);
            spaceShip.loadImage("res/Spaceship.png");
        }
    }

    //Moves each shot in the inputted array of bullets. It also deletes shots that are out of bound or are colliding
    //with obstacles or moving enemies
    private void moveShots(ArrayList<Bullet> shots) {
        ArrayList<Bullet> toDelete = new ArrayList<>();
        for (Bullet shot : shots) {
            shot.move();
            //remove the shot if it moves out of bounds
            if (shot.getX() - getScreenOffset() > width) {
                toDelete.add(shot);
            }
            //remove the shot if it hits an obstacle
            for (Obstacle o : obstacles) {
                checkBulletCollisions(shot, o, toDelete);
            }
            //remove the shot if it hits a moving enemy
            for (MovingEnemy e : mEnemies) {
                checkBulletCollisions(shot, e, toDelete);
            }
        }
        shots.removeAll(toDelete);
    }

    private void checkBulletCollisions(Bullet shot, Obstacle o, ArrayList<Bullet> toDelete) {
        if (shot.getBounds().intersects(o.getBounds())) {
            toDelete.add(shot);
        }
    }

    //Checks collisions for the spaceship and its shots
    private void checkShipCollisions() {
        Rectangle shipHitBox = spaceShip.getBounds();
        for (Obstacle obstacle : obstacles) {
            if (shipHitBox.intersects(obstacle.getBounds())) {
                gameOver = true;
            }
        }
        for (Enemy e : enemies) {
            if (shipHitBox.intersects(e.getBounds())) {
                gameOver = true;
            }
        }
        for (MovingEnemy enemy : mEnemies) {
            if (shipHitBox.intersects(enemy.getBounds())) {
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
                    enemyCounter++;
                }
            }
        }
        enemies.removeAll(toDelete);
    }

    //removes all obstacles, enemies, and moving enemies that are too far out of bounds to the left
    private void checkOutofBounds() {
        ArrayList<Obstacle> outOfBounds = new ArrayList<>();

        for (Obstacle o : obstacles) {
            if (o.getX() - getScreenOffset() + o.getWidth() < -300) {
                outOfBounds.add(o);
            }
        }
        for (Obstacle e : enemies) {
            if (e.getX() - getScreenOffset() + e.getWidth() < -300) {
                outOfBounds.add(e);
            }
        }
        for (Obstacle e : mEnemies) {
            if (e.getX() - getScreenOffset() + e.getWidth() < -300) {
                outOfBounds.add(e);
            }
        }
        obstacles.removeAll(outOfBounds);
        enemies.removeAll(outOfBounds);
        mEnemies.removeAll(outOfBounds);
    }
    
    private int getScreenOffset() {
        return spaceShip.getX() - 300;
    }

    private void spawnObstacles() {
        String[] sprites = {"res/Planet.png", "res/Earth.png", "res/Planet.png", "res/Earth.png", "res/Moon.png", "res/Moon.png"};
        Random r = new Random();

        Obstacle obstacle = new Obstacle(sprites[r.nextInt(sprites.length)], getScreenOffset() + width +
                (int)(Math.random() * 500), (int)(Math.random() * (height - 200)));

        respawnIfOverlap(obstacle);
        obstacles.add(obstacle);
    }

    private void spawnEnemy() {
        Enemy enemy = new Enemy("res/UFO.png", getScreenOffset() + width + (int)(Math.random() * 500),
                (int)(Math.random() * (height - 200)));

        respawnIfOverlap(enemy);
        enemies.add(enemy);
    }

    private void spawnMovingObstacle() {
        MovingEnemy mObstacle = new MovingEnemy("res/Planet.png", getScreenOffset() + width +
                (int)(Math.random() * 500), (int)(Math.random() * (height - 200)));
        respawnIfOverlap(mObstacle);
        mEnemies.add(mObstacle);

        //moving obstacles spawn with moons
        for (int i = 0; i < 5; i++) {
            Obstacle obstacle = new Obstacle("res/Moon.png", mObstacle.getX() + (int)(Math.random() * 300)
                    + 200, (int)(Math.random() * (height - 200)));
            respawnIfOverlap(obstacle);
            obstacles.add(obstacle);
        }
    }

    private void respawnIfOverlap(Obstacle obstacle) {
        while (checkOverlap(obstacle)) {
            obstacle.setX(getScreenOffset() + width + (int)(Math.random() * 500));
            obstacle.setY((int)(Math.random() * (height - 200)));
        }
    }

    private boolean checkOverlap(Obstacle obstacle) {
        for (Obstacle o : obstacles) {
            if (obstacle.getBounds().intersects(o.getBounds())) {
                return true;
            }
        }
        for (Enemy e : enemies) {
            if (obstacle.getBounds().intersects(e.getBounds())) {
                return true;
            }
        }
        for (MovingEnemy e : mEnemies) {
            if (obstacle.getBounds().intersects(e.getBounds())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        backOne.draw(g2d, this, getScreenOffset());
        backTwo.draw(g2d, this, getScreenOffset());

        spaceShip.draw(g2d, this, getScreenOffset());

        for (Bullet shot : shots) {
            if (shipDirection = true) {
                shot.draw(g2d, this, getScreenOffset());
            }
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

        for (MovingEnemy e : mEnemies) {
            e.draw(g2d, this, getScreenOffset());
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString("Enemies killed: " + enemyCounter, 3, 13);

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