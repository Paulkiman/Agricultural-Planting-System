package org.project.agricultural_planting_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantingResponse {

    private Integer recordId;
    private VarietyInfo varietyInfo;
    private LandInfo landInfo;
    private SeedCalculation seedCalculation;
    private FertilizerCalculation fertilizerCalculation;
    private CostBreakdown costBreakdown;
    private YieldPrediction yieldPrediction;
    private String farmerName;
    private String farmLocation;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
    private String notes;
    private LocalDateTime calculatedAt;
}
