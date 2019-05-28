import javafx.scene.shape.Circle;

public class Obstacle extends Sprite {

    public Obstacle(String imageFile, int x, int y) {
        super(imageFile, x, y);
    }

    public void move() { }

    /*@Override
    public Circle getBounds() {
        return new Circle(x + getWidth() / 2, y + getHeight() / 2,getWidth() / 2 - 10);
    }*/
}
