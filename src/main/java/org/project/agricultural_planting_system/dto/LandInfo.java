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
public class LandInfo {
    private BigDecimal lengthMeters;
    private BigDecimal widthMeters;
    private BigDecimal areaSqm;
    private BigDecimal areaAcres;
    private BigDecimal seedSpacingCm;
    private BigDecimal lineSpacingCm;
}
