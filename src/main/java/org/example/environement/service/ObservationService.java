package org.example.environement.service;

import org.example.environement.dto.observation.ObservationDtoReceive;
import org.example.environement.dto.observation.ObservationDtoResponse;
import org.example.environement.entity.Observation;
import org.example.environement.exception.NotFoundException;
import org.example.environement.repository.ObservationRepository;
import org.example.environement.repository.SpecieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final SpecieRepository specieRepository;

    public ObservationService(ObservationRepository observationRepository, SpecieRepository specieRepository) {
        this.observationRepository = observationRepository;
        this.specieRepository = specieRepository;
    }

    public ObservationDtoResponse create(ObservationDtoReceive observation) {
        return observationRepository
                .save(observation.dtoToEntity(specieRepository))
                .entityToDto();
    }

    public List<ObservationDtoResponse> get(int pageSize, int pageNumber) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return convertList(observationRepository
                .findAll(PageRequest.of(pageNumber, pageSize, sort))
                .getContent());
    }

    public ObservationDtoResponse get(long id) {
        return observationRepository.findById(id)
                .orElseThrow(NotFoundException::new)
                .entityToDto();
    }

    public List<ObservationDtoResponse> getByLocation(String location) {
        return convertList(observationRepository.findObservationByLocationIsLike(location));
    }

    public List<ObservationDtoResponse> getBySpecie(long specieId) {
        return convertList(observationRepository.findObservationBySpecieId(specieId));
    }

    private List<ObservationDtoResponse> convertList(List<Observation> observations) {
        return observations.stream()
                .map(Observation::entityToDto)
                .collect(Collectors.toList());
    }
}
