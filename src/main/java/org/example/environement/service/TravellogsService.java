package org.example.environement.service;

import org.example.environement.dto.travellogs.TravellogDtoReceive;
import org.example.environement.dto.travellogs.TravellogDtoResponse;
import org.example.environement.dto.travellogs.TravellogDtoStat;
import org.example.environement.entity.Observation;
import org.example.environement.entity.Travellog;
import org.example.environement.exception.NotFoundException;
import org.example.environement.repository.ObservationRepository;
import org.example.environement.repository.TravellogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TravellogsService {

    private final TravellogRepository travellogRepository;
    private final ObservationRepository observationRepository;

    public TravellogsService(TravellogRepository travellogRepository, ObservationRepository observationRepository) {
        this.travellogRepository = travellogRepository;
        this.observationRepository = observationRepository;
    }

    public TravellogDtoResponse create(TravellogDtoReceive dtoReceive) {
        Observation observation = observationRepository.findById(dtoReceive.getObservationId())
                .orElseThrow(NotFoundException::new);
        Travellog travellog = dtoReceive.dtoToEntity(observation);
        Travellog save = travellogRepository.save(travellog);
        return save.entityToDto();
    }

    public List<TravellogDtoResponse> get(int limit) {
        return travellogRepository.findAll()
                .stream()
                .limit(limit)
                .map(Travellog::entityToDto)
                .collect(Collectors.toList());
    }

    public TravellogDtoStat getStat(long observationId) {
        List<Travellog> logs = travellogRepository.findTravellogByObservation_Id(observationId);
        return buildStat(logs);
    }

    public Map<String, TravellogDtoStat> getStatForUserLastMonth(String userName) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Travellog> logs = travellogRepository.findTravellogByUserForLastMonth(userName, oneMonthAgo);

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getObservation().getObserverName(),
                        Collectors.collectingAndThen(Collectors.toList(), this::buildStat)
                ));
    }

    private TravellogDtoStat buildStat(List<Travellog> logs) {
        TravellogDtoStat stat = new TravellogDtoStat();

        for (Travellog log : logs) {
            double distance = log.getDistanceKm();
            double emission = log.getEstimatedCo2Kg();
            String mode = log.getMode().name();

            stat.addTotalDistanceKm(distance);
            stat.addTotalEmissionsKg(emission);

            stat.getByMode().merge(mode, distance, Double::sum);
        }

        return stat;
    }
}
