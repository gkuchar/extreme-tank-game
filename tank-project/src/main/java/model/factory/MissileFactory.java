package model.factory;

import model.*;

public class MissileFactory {
    private MissileFactory() {}

    public static Missile createMissile(double x, double y, Direction direction, Tank owner) {
        return new Missile(x, y, direction, owner);
    }
}

