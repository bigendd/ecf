package org.example.environement.controller;

import org.example.environement.dto.observation.ObservationDtoReceive;
import org.example.environement.dto.observation.ObservationDtoResponse;
import org.example.environement.service.ObservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observation")
public class ObservationController {

    private final ObservationService observationService;

    public ObservationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @GetMapping
    public ResponseEntity<List<ObservationDtoResponse>> getObservations(
            @RequestParam int pageSize,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(observationService.get(pageSize, pageNumber));
    }
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationDtoResponse> getObservationById(@PathVariable long id) {
        return ResponseEntity.ok(observationService.get(id));
    }

    @PostMapping
    public ResponseEntity<ObservationDtoResponse> createObservation(@RequestBody ObservationDtoReceive dtoReceive) {
        ObservationDtoResponse response = observationService.create(dtoReceive);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/location")
    public ResponseEntity<List<ObservationDtoResponse>> getObservationsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(observationService.getByLocation(location));
    }

    @GetMapping("/specie/{specieId}")
    public ResponseEntity<List<ObservationDtoResponse>> getObservationsBySpecie(@PathVariable long specieId) {
        return ResponseEntity.ok(observationService.getBySpecie(specieId));
    }
}
