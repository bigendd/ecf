package org.example.environement.repository;

import org.example.environement.entity.Travellog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TravellogRepository extends JpaRepository<Travellog, Long> {
    @Query("SELECT t FROM Travellog t WHERE t.observation.observerName = :user AND t.observation.observationDate > :date")
    List<Travellog> findTravellogByUserForLastMonth(@Param("user") String user, @Param("date") LocalDate date);

    List<Travellog> findTravellogByObservation_Id(long id);
}
