public class Bullet extends Sprite {

    private int bulletSpeed = 10;

    public Bullet(String imageFile, int x, int y) {
        super(imageFile, x, y);
        xVel = bulletSpeed;
    }

    //This constructor is used by enemies to point at the player
    public Bullet(String imageFile, int x, int y, int xTarget, int yTarget) {
        super(imageFile, x, y);
        double xDiff = Math.abs(xTarget - x);
        double yDiff = Math.abs(yTarget - y);

        //points at the target by using similar triangles and essentially sets the speed of the hypotenuse to 10
        double angle = Math.atan(yDiff/xDiff);
        xVel = bulletSpeed * Math.cos(angle);
        yVel = bulletSpeed * Math.sin(angle);
        System.out.println(xVel + " " + yVel);
    }

    public void move(){
        x += xVel;
    }


}
