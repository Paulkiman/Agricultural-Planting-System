package org.project.agricultural_planting_system.repository;

import org.project.agricultural_planting_system.model.PlantingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlantingRecordRepository extends JpaRepository<PlantingRecord, Integer> {

    List<PlantingRecord> findByVariety_VarietyId(Integer varietyId);

    List<PlantingRecord> findByFarmerName(String farmerName);

    List<PlantingRecord> findByPlantingDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT pr FROM PlantingRecord pr " +
            "JOIN FETCH pr.variety " +
            "WHERE pr.farmerName = :farmerName " +
            "ORDER BY pr.createdAt DESC")
    List<PlantingRecord> findByFarmerNameWithVariety(@Param("farmerName") String farmerName);

    @Query("SELECT pr FROM PlantingRecord pr " +
            "JOIN FETCH pr.variety " +
            "ORDER BY pr.createdAt DESC")
    List<PlantingRecord> findAllWithVarietyOrderByCreatedAtDesc();

    @Query("SELECT pr FROM PlantingRecord pr " +
            "JOIN FETCH pr.variety v " +
            "WHERE v.varietyId = :varietyId " +
            "ORDER BY pr.createdAt DESC")
    List<PlantingRecord> findByVarietyIdOrderByCreatedAtDesc(@Param("varietyId") Integer varietyId);

    @Query("SELECT COUNT(pr) FROM PlantingRecord pr WHERE pr.variety.varietyId = :varietyId")
    Long countByVarietyId(@Param("varietyId") Integer varietyId);

    @Query("SELECT pr FROM PlantingRecord pr " +
            "WHERE pr.plantingDate >= :startDate " +
            "ORDER BY pr.createdAt DESC")
    List<PlantingRecord> findRecentPlantings(@Param("startDate") LocalDate startDate);
}
