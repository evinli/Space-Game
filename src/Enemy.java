public class Enemy extends Obstacle {

    private static final int bulletSpeed = 5;
    private int counter = 0;

    public Enemy(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    //creates a new bullet from the enemy directed at the spaceship
    public Bullet shoot(int shipX, int shipY) {
        return new Bullet("res/thing_test.png", x - 10, y + getHeight() / 2, shipX, shipY, bulletSpeed);
        //note the bullet has to spawn at x - 10 so it doesn't start intersecting moving enemies that shoot
    }

    //determines if the enemy should shoot.
    //enemies only shoot if they are on the screen and if they are in front of the spaceship
    public boolean shouldShoot(int screenOffset, int screenWidth, Spaceship spaceShip) {
        counter++;
        if (counter % 20 == 0 &&
                getX() - screenOffset <= screenWidth && getX() > spaceShip.getX() + spaceShip.getWidth())
            return true;
        return false;
    }

    @Override
    public void move() {
        super.move();
    }
}
