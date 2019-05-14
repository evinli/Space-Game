public class Bullet extends Sprite {

    public Bullet(String imageFile, int x, int y) {
        super(imageFile, x, y);
        xVel = 5;
    }

    public void move(){
        x += xVel;
    }


}
