package org.example.environement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.environement.dto.travellogs.TravellogDtoResponse;
import org.example.environement.entity.enums.TravelMode;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Travellog {
    @Id
    @GeneratedValue
    private long id;

    private double distanceKm;

    @Enumerated(EnumType.STRING)
    private TravelMode mode;

    private double estimatedCo2Kg;

    @ManyToOne
    @JoinColumn(name = "observation_id")
    private Observation observation;

    public void calculateCO2() {
        double emissionFactor;
        switch (mode) {
            case WALKING, BIKE -> emissionFactor = 0;
            case CAR -> emissionFactor = 0.22;
            case BUS -> emissionFactor = 0.11;
            case TRAIN -> emissionFactor = 0.03;
            case PLANE -> emissionFactor = 0.259;
            default -> emissionFactor = 0;
        }
        this.estimatedCo2Kg = this.distanceKm * emissionFactor;
    }

    public TravellogDtoResponse entityToDto() {
        return TravellogDtoResponse.builder()
                .id(this.getId())
                .distanceKm(this.getDistanceKm())
                .mode(this.getMode().name())
                .estimatedCo2Kg(this.getEstimatedCo2Kg())
                .build();
    }
}
