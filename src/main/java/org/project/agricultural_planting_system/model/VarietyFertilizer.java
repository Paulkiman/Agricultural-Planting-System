package org.project.agricultural_planting_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "variety_fertilizer", uniqueConstraints = {
        @UniqueConstraint(name = "unique_variety_fertilizer",
                columnNames = {"variety_id", "fertilizer_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarietyFertilizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variety_fertilizer_id")
    private Integer varietyFertilizerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id", nullable = false)
    @ToString.Exclude
    private MaizeVariety variety;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fertilizer_id", nullable = false)
    @ToString.Exclude
    private Fertilizer fertilizer;

    @Column(name = "required_amount_grams", nullable = false, precision = 6, scale = 3)
    @NotNull(message = "Required amount is required")
    @DecimalMin(value = "0.001", message = "Required amount must be positive")
    private BigDecimal requiredAmountGrams;

    @Column(name = "application_method", length = 100)
    private String applicationMethod;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
