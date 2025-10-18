package org.project.agricultural_planting_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "maize_variety")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaizeVariety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variety_id")
    private Integer varietyId;

    @Column(name = "variety_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Variety name is required")
    @Size(max = 100, message = "Variety name must not exceed 100 characters")
    private String varietyName;

    @Column(name = "variety_code", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Variety code is required")
    private String varietyCode;

    @Column(name = "seed_price_per_kg", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Seed price is required")
    @Positive(message = "Seed price must be positive")
    private BigDecimal seedPricePerKg;

    @Column(name = "average_cob_mass_kg", nullable = false, precision = 5, scale = 2)
    @NotNull(message = "Average cob mass is required")
    @Positive(message = "Average cob mass must be positive")
    private BigDecimal averageCobMassKg;

    @Column(name = "expected_crops_per_acre", nullable = false)
    @NotNull(message = "Expected crops per acre is required")
    @Positive(message = "Expected crops must be positive")
    private Integer expectedCropsPerAcre;

    @Column(name = "maturity_period_days", nullable = false)
    @NotNull(message = "Maturity period is required")
    @Positive(message = "Maturity period must be positive")
    private Integer maturityPeriodDays;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "variety", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<VarietyFertilizer> varietyFertilizers = new HashSet<>();

    @OneToMany(mappedBy = "variety", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<PlantingRecord> plantingRecords = new HashSet<>();
}