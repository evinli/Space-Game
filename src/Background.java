public class Background extends Sprite {

    public Background(int x) {
        super("res/Backdrop.png", x, 0);
    }

    public void move() {
    }

    public void update(int frameWidth, int screenOffset) {
        //if background is off screen on the left
//        if (x + getWidth() < screenOffset) {
//            x += getWidth() * 2;
//        } else if (x - screenOffset > frameWidth) {//off screen on the right
//            x -= getWidth() * 2;
//        }
    }
}
