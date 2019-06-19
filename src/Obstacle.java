import java.awt.*;

//This class is for regular stationary planets/obstacles
public class Obstacle extends Sprite {

    public Obstacle(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    //getBounds has to take a smaller rectangle than the image size to remove whitespace
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 20, y + 20, getWidth() - 40, getHeight() - 40);
    }
}
