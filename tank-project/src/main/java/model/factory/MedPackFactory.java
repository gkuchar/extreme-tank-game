package model.factory;

import model.MedPack;

public class MedPackFactory {

    private MedPackFactory() {}

    public static MedPack createMedPack(double x, double y, double width, double height) {
        return new MedPack(x, y, width, height);
    }
}
