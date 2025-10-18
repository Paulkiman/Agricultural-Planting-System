package org.project.agricultural_planting_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedCalculation {
    private Integer totalSeedsRequired;
    private BigDecimal seedWeightKg;
    private BigDecimal seedCost;
    private Integer seedsPerRow;
    private Integer numberOfRows;
}
