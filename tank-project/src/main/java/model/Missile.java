package model;

public class Missile extends GameObject {
    private final Direction direction;
    private final double speed = 500.0;
    private final Tank owner;

    public Missile(double x, double y, Direction direction, Tank owner) {
        super(x, y, 8, 8);
        this.direction = direction;
        this.owner = owner;
    }

    @Override
    public void update(double dt) {
        // Move straight in the direction fired
        x += direction.dx * speed * dt;
        y += direction.dy * speed * dt;
    }

    public Direction getDirection() {
        return direction;
    }

    public Tank getOwner() {
        return owner;
    }
}
