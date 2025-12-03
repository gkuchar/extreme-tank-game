package model.strategies;

import model.Direction;
import model.Tank;

public class PlayerMovementStrategy implements MovementStrategy {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public void setInput(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    @Override
    public void move(Tank tank, double dt) {
        double dx = 0;
        double dy = 0;

        if (up)    dy -= 1;
        if (down)  dy += 1;
        if (left)  dx -= 1;
        if (right) dx += 1;

        // no movement
        if (dx == 0 && dy == 0) return;

        // normalize the vector (fixes diagonal speed)
        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        // update direction for sprite rotation
        if (Math.abs(dx) > Math.abs(dy)) {
            tank.setDirection(dx > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
            tank.setDirection(dy > 0 ? Direction.DOWN : Direction.UP);
        }

        // apply movement
        double newX = tank.getX() + dx * tank.getSpeed() * dt;
        double newY = tank.getY() + dy * tank.getSpeed() * dt;

        tank.setX(newX);
        tank.setY(newY);
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}

