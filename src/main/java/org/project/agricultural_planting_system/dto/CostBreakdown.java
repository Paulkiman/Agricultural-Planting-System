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
public class CostBreakdown {
    private BigDecimal seedCost;
    private BigDecimal dapCost;
    private BigDecimal npkCost;
    private BigDecimal totalFertilizerCost;
    private BigDecimal totalInputCost;
    private BigDecimal costPerAcre;
    private BigDecimal costPerSqm;
}
