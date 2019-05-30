import java.awt.*;

public class Spaceship extends Sprite {

    Spaceship(int x, int y) {
        super("res/Spaceship.png", x, y);
        xVel = 0;
        yVel = 0;
    }

    public void move() {

    }

    public Bullet shoot() {
        return new Bullet("res/Bullet.png", x + getWidth(), y + getHeight() / 2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 5, y + 20, getWidth() - 10, getHeight() - 40);
    }
}
