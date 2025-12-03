package model.factory;

import model.Explosion;

public class ExplosionFactory {

    private ExplosionFactory() {}

    public static Explosion createExplosion(double x, double y, double width, double height) {
        return new Explosion(x, y, width, height);
    }
}
