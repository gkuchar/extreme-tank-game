package model;

import model.strategies.PlayerMovementStrategy;

public class PlayerTank extends Tank {

    public PlayerTank(double x, double y) {
        super(x, y, 30, 30, new PlayerMovementStrategy());
    }

    @Override
    public PlayerMovementStrategy getMovementStrategy() {
        return (PlayerMovementStrategy) movementStrategy;
    }
}
