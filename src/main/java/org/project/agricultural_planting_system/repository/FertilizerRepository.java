package org.project.agricultural_planting_system.repository;

import org.project.agricultural_planting_system.model.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Integer> {

    Optional<Fertilizer> findByFertilizerType(String fertilizerType);

    Optional<Fertilizer> findByFertilizerCode(String fertilizerCode);

    List<Fertilizer> findByIsActiveTrue();

    @Query("SELECT f FROM Fertilizer f WHERE f.isActive = true ORDER BY f.fertilizerType")
    List<Fertilizer> findAllActiveFertilizersOrdered();

    boolean existsByFertilizerType(String fertilizerType);
}
