public class Spaceship extends Sprite {

    Spaceship(int x, int y) {
        super("res/Spaceship.png", x, y);
        xVel = 0;
        yVel = 0;
    }

    public void move() {

    }
    
    public Bullet shoot() {
        return new Bullet("res/Bullet.png", x + width, y + height / 2);
    }

}
