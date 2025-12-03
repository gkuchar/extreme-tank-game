package model;

public class Explosion extends GameObject {
    private double lifeTime = 0.5;   // explosion lasts half a second

    public Explosion(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double dt) {
        lifeTime -= dt;
        if (lifeTime <= 0) {
            destroy();
        }
    }

    public double getLifeTime() {
        return lifeTime;
    }
}
