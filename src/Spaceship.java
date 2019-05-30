import java.awt.*;

public class Spaceship extends Sprite {

    private static final int bulletSpeed = 10;

    Spaceship(int x, int y) {
        super("res/Spaceship.png", x, y);
        xVel = 0;
        yVel = 0;
    }

    public void move() {

    }

    public Bullet shoot() {
        return new Bullet("res/Bullet.png", x + getWidth(), y + getHeight() / 2, bulletSpeed);
    }

    public Rectangle getBounds() {
        return new Rectangle(x + 10, y + 20, getWidth() - 15, getHeight() - 40);
    }
}
