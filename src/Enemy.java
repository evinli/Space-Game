public class Enemy extends Obstacle {

    public Enemy(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    public Bullet shoot(int shipX, int shipY) {
        return new Bullet("res/thing_test.png", x, y + getHeight() / 2, shipX, shipY);
    }

    @Override
    public void move() {
        super.move();
    }
}
