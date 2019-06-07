import java.awt.*;

public class Obstacle extends Sprite {

    public Obstacle(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    public void move() { }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 20, y + 20, getWidth() - 40, getHeight() - 40);
    }
}
