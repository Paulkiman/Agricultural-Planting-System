package org.project.agricultural_planting_system.repository;

import org.project.agricultural_planting_system.model.VarietyFertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarietyFertilizerRepository extends JpaRepository<VarietyFertilizer, Integer> {

    List<VarietyFertilizer> findByVariety_VarietyId(Integer varietyId);

    List<VarietyFertilizer> findByFertilizer_FertilizerId(Integer fertilizerId);

    @Query("SELECT vf FROM VarietyFertilizer vf " +
            "JOIN FETCH vf.variety v " +
            "JOIN FETCH vf.fertilizer f " +
            "WHERE v.varietyId = :varietyId AND f.fertilizerType = :fertilizerType")
    Optional<VarietyFertilizer> findByVarietyIdAndFertilizerType(
            @Param("varietyId") Integer varietyId,
            @Param("fertilizerType") String fertilizerType
    );

    @Query("SELECT vf FROM VarietyFertilizer vf " +
            "JOIN FETCH vf.variety " +
            "JOIN FETCH vf.fertilizer " +
            "WHERE vf.variety.varietyId = :varietyId")
    List<VarietyFertilizer> findAllByVarietyIdWithFertilizers(@Param("varietyId") Integer varietyId);

    boolean existsByVariety_VarietyIdAndFertilizer_FertilizerId(
            Integer varietyId,
            Integer fertilizerId
    );
}
