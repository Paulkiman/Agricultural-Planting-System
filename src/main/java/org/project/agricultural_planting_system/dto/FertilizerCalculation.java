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
public class FertilizerCalculation {
    private FertilizerDetail dap;
    private FertilizerDetail npk;
    private BigDecimal totalFertilizerWeightKg;
    private BigDecimal totalFertilizerCost;
}
