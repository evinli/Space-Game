import java.awt.*;

public class Obstacle extends Sprite {

    public Obstacle(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    public void move() { }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 10, y + 10, getWidth() - 20, getHeight() - 20);
    }
}
