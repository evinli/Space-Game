import java.awt.*;

public class MovingEnemy extends Enemy {

    public MovingEnemy(String imageFile, int x, int y) {
        super(imageFile, x, y);
        yVel = 2;
    }

    public void changeDirection() {
        yVel *= -1;
    }

    @Override
    public void move() {
        y += yVel;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 20, y + 20, getWidth() - 40, getHeight() - 40);
    }
}
