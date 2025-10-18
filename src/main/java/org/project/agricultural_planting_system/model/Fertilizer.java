package org.project.agricultural_planting_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fertilizer", indexes = {
        @Index(name = "idx_fertilizer_type", columnList = "fertilizer_type"),
        @Index(name = "idx_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fertilizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fertilizer_id")
    private Integer fertilizerId;

    @Column(name = "fertilizer_type", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Fertilizer type is required")
    private String fertilizerType;

    @Column(name = "fertilizer_code", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Fertilizer code is required")
    private String fertilizerCode;

    @Column(name = "price_per_kg", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Fertilizer price is required")
    @DecimalMin(value = "0.01", message = "Price must be positive")
    private BigDecimal pricePerKg;

    @Column(name = "amount_per_seed_grams", nullable = false, precision = 6, scale = 3)
    @NotNull(message = "Amount per seed is required")
    @DecimalMin(value = "0.001", message = "Amount must be positive")
    private BigDecimal amountPerSeedGrams;

    @Column(name = "nutrient_composition", length = 100)
    private String nutrientComposition;

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

    @OneToMany(mappedBy = "fertilizer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<VarietyFertilizer> varietyFertilizers = new HashSet<>();
}
