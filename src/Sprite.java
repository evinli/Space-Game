import java.awt.Image;
import javax.swing.ImageIcon;

public abstract class Sprite {
    private Image image;
    private int width, height;
    protected int x, y;
    protected int xVel, yVel;

    public Sprite(String imageFile, int x, int y) {
        loadImage(imageFile);
        this.x = x;
        this.y = y;

    }

    private void loadImage(String imageName) {
        ImageIcon i = new ImageIcon(imageName);
        image = i.getImage();
    }

    public abstract void move();

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
