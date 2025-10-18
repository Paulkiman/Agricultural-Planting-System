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
public class FertilizerDetail {
    private String fertilizerType;
    private BigDecimal gramsPerSeed;
    private BigDecimal totalWeightKg;
    private BigDecimal pricePerKg;
    private BigDecimal totalCost;
}
