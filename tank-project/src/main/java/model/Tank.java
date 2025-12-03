package model;

import model.events.GameEventBus;
import model.events.GameEventType;
import model.strategies.*;
import model.factory.*;

public abstract class Tank extends GameObject {
    protected int health;
    protected double speed;
    protected Direction direction;
    protected MovementStrategy movementStrategy;

    public Tank(double x, double y, double w, double h, MovementStrategy strategy) {
        super(x, y, w, h);
        this.health = 100;
        this.speed = 200.0;
        this.direction = Direction.UP;
        this.movementStrategy = strategy;
    }

    @Override
    public void update(double dt) {
        movementStrategy.move(this, dt);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public Missile fire() {
        double mx = getX() + getWidth() / 2;
        double my = getY() + getHeight() / 2;
        return MissileFactory.createMissile(mx, my, direction, this);
    }

    public void damage(int amount) {
        health -= amount;
        if (this instanceof PlayerTank) {
            GameEventBus.getInstance().fireEvent(
                    GameEventType.PLAYER_HEALTH_CHANGED,
                    this
            );
        }
        if (health <= 0) destroy();
    }
}
