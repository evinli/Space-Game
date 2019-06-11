import java.util.Random;

public class MovingEnemy extends Enemy {

    public MovingEnemy(String imageFile, int x, int y) {
        super(imageFile, x, y);
        yVel = 2;
        Random r = new Random();
        if (r.nextBoolean())
            changeDirection();
    }

    public void changeDirection() {
        yVel *= -1;
    }

    @Override
    public void move() {
        y += yVel;
    }
}
