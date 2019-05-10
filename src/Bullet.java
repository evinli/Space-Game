public class Bullet extends Sprite {

    public Bullet(String imageFile, int x, int y) {
        super(imageFile, x, y);
        xVel = 2;
    }

    public void move(){
        x += xVel;
    }


}
