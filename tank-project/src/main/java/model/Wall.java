package model;

public class Wall extends GameObject {

    public Wall(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double dt) {
        // Walls don't move or change over time
    }

    @Override
    public void destroy() {
        // Walls cannot be destroyed
        // so override destroy() to do nothing
    }
}
