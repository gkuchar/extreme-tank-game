package model.factory;

import model.*;

public class WallFactory {
    private WallFactory() {}

    public static Wall createWall(double x, double y, double width, double height) {
        return new Wall(x, y, width, height);
    }
}
