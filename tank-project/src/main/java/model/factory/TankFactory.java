package model.factory;

import model.*;

public class TankFactory {

    private TankFactory() {}

    public static Tank createTank(TankType type, double x, double y) {
        return switch (type) {
            case ENEMY -> new EnemyTank(x, y);
            case PLAYER -> new PlayerTank(x, y);
        };
    }
}
