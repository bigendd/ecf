package org.example.environement.entity.enums;

public enum TravelMode {
    WALKING(0.0),
    BIKE(0.0),
    CAR(0.22),
    BUS(0.11),
    TRAIN(0.03),
    PLANE(0.259);

    private final double emissionFactor;

    TravelMode(double emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public double getEmissionFactor() {
        return emissionFactor;
    }
}
