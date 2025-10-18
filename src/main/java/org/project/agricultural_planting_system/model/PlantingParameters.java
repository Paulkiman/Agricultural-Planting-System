package org.project.agricultural_planting_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "planting_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantingParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    private Integer parameterId;

    @Column(name = "seed_spacing_cm", nullable = false, precision = 6, scale = 2)
    @NotNull(message = "Seed spacing is required")
    @DecimalMin(value = "1.0", message = "Seed spacing must be at least 1 cm")
    @Builder.Default
    private BigDecimal seedSpacingCm = new BigDecimal("30.00");

    @Column(name = "line_spacing_cm", nullable = false, precision = 6, scale = 2)
    @NotNull(message = "Line spacing is required")
    @DecimalMin(value = "1.0", message = "Line spacing must be at least 1 cm")
    @Builder.Default
    private BigDecimal lineSpacingCm = new BigDecimal("70.00");

    @Column(name = "parameter_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Parameter name is required")
    private String parameterName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
