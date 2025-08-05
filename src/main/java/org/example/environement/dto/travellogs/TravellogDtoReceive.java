package org.example.environement.dto.travellogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.environement.entity.Observation;
import org.example.environement.entity.Travellog;
import org.example.environement.entity.enums.TravelMode;
import org.example.environement.repository.ObservationRepository;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TravellogDtoReceive {
    private double distanceKm;
    private String mode;
    private Long observationId;

    public Travellog dtoToEntity(Observation observation){
        Travellog travellog = Travellog.builder()
                .distanceKm(this.getDistanceKm())
                .mode(TravelMode.valueOf(this.getMode()))
                .observation(observation)
                .build();

        travellog.calculateCO2();
        return travellog;
    }
}
