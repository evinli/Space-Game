public class Enemy extends Obstacle {

    private static final int bulletSpeed = 5;

    public Enemy(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    public Bullet shoot(int shipX, int shipY) {
        return new Bullet("res/thing_test.png", x, y + getHeight() / 2, shipX, shipY, bulletSpeed);
    }

    @Override
    public void move() {
        super.move();
    }
}
