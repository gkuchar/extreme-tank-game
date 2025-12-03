package model.strategies;

import model.Direction;
import model.Tank;

public class AIMovementStrategy implements MovementStrategy {
    private double moveTimer = 0;
    private double shotTimer = 0;
    private boolean fireRequested = false;

    @Override
    public void move(Tank tank, double dt) {
        moveTimer += dt;
        shotTimer += dt;

        if (moveTimer > .5) {
            moveTimer = 0;
            Direction[] dirs = Direction.values();
            tank.setDirection(dirs[(int)(Math.random() * dirs.length)]);
        }

        if (shotTimer > .75) {
            shotTimer = 0;
            fireRequested = true;
        }

        double newX = tank.getX() + tank.getSpeed() * dt * tank.getDirection().dx;
        double newY = tank.getY() + tank.getSpeed() * dt * tank.getDirection().dy;

        tank.setX(newX);
        tank.setY(newY);
    }

    public boolean shouldFire() {
        return fireRequested;
    }

    public void resetFire() {
        fireRequested = false;
    }
}
