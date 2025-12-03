package model;

import model.strategies.AIMovementStrategy;

public class EnemyTank extends Tank {


    public EnemyTank(double x, double y) {
        super(
                x,
                y,
                30,
                30,
                new AIMovementStrategy()
        );
    }
}

