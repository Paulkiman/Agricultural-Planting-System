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
public class YieldPrediction {
    private BigDecimal expectedYieldKg;
    private Integer expectedPackages90kg;
    private BigDecimal marketPricePerPackage;
    private BigDecimal predictedRevenue;
    private BigDecimal profitMargin;
    private BigDecimal profitMarginPercentage;
    private BigDecimal revenuePerAcre;
    private BigDecimal returnOnInvestment;
}
