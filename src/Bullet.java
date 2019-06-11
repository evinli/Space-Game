public class Bullet extends Sprite {

    public Bullet(String imageFile, int x, int y, int speed) {
        super(imageFile, x, y);
        xVel = speed;
        yVel = 0;
    }

    public Bullet(String imageFile, int x, int y, int speed, boolean direction) {
        super(imageFile, x, y);
        if (direction) {
            xVel = speed;
        }
        // speed of bullet assumes negative velocity when spaceship is facing left
        else {
            xVel = 0 - speed;
        }
        yVel = 0;
    }

    //This constructor is used by enemies to point at the player
    public Bullet(String imageFile, int x, int y, int xTarget, int yTarget, int speed) {
        super(imageFile, x, y);
        double xDiff = xTarget - x;
        double yDiff = yTarget - y;

        //points at the target by using similar triangles and essentially sets the speed of the hypotenuse to 10
        double angle = Math.atan(yDiff/xDiff);
        xVel = -1 * speed * Math.cos(angle);
        yVel = -1 * speed * Math.sin(angle);
    }

    public void move(){
        x += xVel;
        y += yVel;
    }


}
