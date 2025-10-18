package org.project.agricultural_planting_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planting_records", indexes = {
        @Index(name = "idx_variety_id", columnList = "variety_id"),
        @Index(name = "idx_planting_date", columnList = "planting_date"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id", nullable = false)
    @NotNull(message = "Variety is required")
    @ToString.Exclude
    private MaizeVariety variety;

    @Column(name = "farmer_name")
    private String farmerName;

    @Column(name = "farm_location")
    private String farmLocation;

    @Column(name = "land_length_meters", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Land length is required")
    @DecimalMin(value = "0.01", message = "Land length must be positive")
    private BigDecimal landLengthMeters;

    @Column(name = "land_width_meters", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Land width is required")
    @DecimalMin(value = "0.01", message = "Land width must be positive")
    private BigDecimal landWidthMeters;

    @Column(name = "land_area_sqm", nullable = false, precision = 12, scale = 2)
    private BigDecimal landAreaSqm;

    @Column(name = "land_area_acres", nullable = false, precision = 10, scale = 4)
    private BigDecimal landAreaAcres;

    @Column(name = "total_seeds_required", nullable = false)
    private Integer totalSeedsRequired;

    @Column(name = "seed_weight_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal seedWeightKg;

    @Column(name = "seed_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal seedCost;

    @Column(name = "dap_weight_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal dapWeightKg;

    @Column(name = "dap_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal dapCost;

    @Column(name = "npk_weight_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal npkWeightKg;

    @Column(name = "npk_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal npkCost;

    @Column(name = "total_fertilizer_weight_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalFertilizerWeightKg;

    @Column(name = "total_input_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalInputCost;

    @Column(name = "expected_yield_kg", nullable = false, precision = 12, scale = 2)
    private BigDecimal expectedYieldKg;

    @Column(name = "expected_packages_90kg", nullable = false)
    private Integer expectedPackages90kg;

    @Column(name = "market_price_per_package", nullable = false, precision = 10, scale = 2)
    private BigDecimal marketPricePerPackage;

    @Column(name = "predicted_revenue", nullable = false, precision = 12, scale = 2)
    private BigDecimal predictedRevenue;

    @Column(name = "profit_margin", nullable = false, precision = 12, scale = 2)
    private BigDecimal profitMargin;

    @Column(name = "planting_date")
    private LocalDate plantingDate;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
