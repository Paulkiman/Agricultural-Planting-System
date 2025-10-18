package org.project.agricultural_planting_system.repository;

import org.project.agricultural_planting_system.model.PlantingParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantingParametersRepository extends JpaRepository<PlantingParameters, Integer> {

    Optional<PlantingParameters> findByIsDefaultTrue();

    Optional<PlantingParameters> findByParameterName(String parameterName);

    List<PlantingParameters> findByIsActiveTrue();

    boolean existsByParameterName(String parameterName);
}
