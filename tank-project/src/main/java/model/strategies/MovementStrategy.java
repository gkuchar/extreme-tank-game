package model.strategies;

import model.Tank;

public interface MovementStrategy {
    void move(Tank tank, double dt);
}
