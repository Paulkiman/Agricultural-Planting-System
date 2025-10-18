package org.project.agricultural_planting_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantingRequest {

    @NotNull(message = "Variety ID is required")
    @Positive(message = "Variety ID must be positive")
    private Integer varietyId;

    @NotNull(message = "Land length is required")
    @DecimalMin(value = "1.0", message = "Land length must be at least 1 meter")
    @DecimalMax(value = "100000.0", message = "Land length cannot exceed 100000 meters")
    private BigDecimal landLengthMeters;

    @NotNull(message = "Land width is required")
    @DecimalMin(value = "1.0", message = "Land width must be at least 1 meter")
    @DecimalMax(value = "100000.0", message = "Land width cannot exceed 100000 meters")
    private BigDecimal landWidthMeters;

    @NotNull(message = "DAP price is required")
    @DecimalMin(value = "1.0", message = "DAP price must be positive")
    private BigDecimal dapPricePerKg;

    @NotNull(message = "NPK price is required")
    @DecimalMin(value = "1.0", message = "NPK price must be positive")
    private BigDecimal npkPricePerKg;

    @NotNull(message = "Market price per package is required")
    @DecimalMin(value = "1.0", message = "Market price must be positive")
    private BigDecimal marketPricePerPackage;

    private String farmerName;
    private String farmLocation;
    private LocalDate plantingDate;
    private String notes;
}
