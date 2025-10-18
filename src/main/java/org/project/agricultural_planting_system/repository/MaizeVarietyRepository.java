package org.project.agricultural_planting_system.repository;

import org.project.agricultural_planting_system.model.MaizeVariety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaizeVarietyRepository extends JpaRepository<MaizeVariety, Integer> {

    Optional<MaizeVariety> findByVarietyName(String varietyName);

    Optional<MaizeVariety> findByVarietyCode(String varietyCode);

    List<MaizeVariety> findByIsActiveTrue();

    @Query("SELECT mv FROM MaizeVariety mv WHERE mv.isActive = true ORDER BY mv.varietyName")
    List<MaizeVariety> findAllActiveVarietiesOrdered();

    boolean existsByVarietyName(String varietyName);

    boolean existsByVarietyCode(String varietyCode);
}
