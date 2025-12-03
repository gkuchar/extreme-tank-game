package model;

public class MedPack extends GameObject {

    public MedPack(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double dt) {
        // MedPacks don’t move or animate (model-side)
    }
}
