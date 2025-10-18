package org.project.agricultural_planting_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.dto.VarietyInfo;
import org.project.agricultural_planting_system.model.MaizeVariety;
import org.project.agricultural_planting_system.service.MaizeVarietyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/varieties")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Maize Varieties", description = "APIs for managing maize varieties")
@CrossOrigin(origins = "*")
public class MaizeVarietyController {

    private final MaizeVarietyService maizeVarietyService;

    @GetMapping
    @Operation(summary = "Get all active varieties",
            description = "Retrieve all active maize varieties")
    public ResponseEntity<List<VarietyInfo>> getAllActiveVarieties() {
        log.info("Fetching all active maize varieties");

        List<VarietyInfo> varieties = maizeVarietyService.getAllActiveVarieties();

        return ResponseEntity.ok(varieties);
    }

    @GetMapping("/{varietyId}")
    @Operation(summary = "Get variety by ID",
            description = "Retrieve a specific maize variety by its ID")
    public ResponseEntity<VarietyInfo> getVarietyById(@PathVariable Integer varietyId) {
        log.info("Fetching variety with ID: {}", varietyId);

        VarietyInfo variety = maizeVarietyService.getVarietyById(varietyId);

        return ResponseEntity.ok(variety);
    }

    @GetMapping("/code/{varietyCode}")
    @Operation(summary = "Get variety by code",
            description = "Retrieve a specific maize variety by its code")
    public ResponseEntity<VarietyInfo> getVarietyByCode(@PathVariable String varietyCode) {
        log.info("Fetching variety with code: {}", varietyCode);

        VarietyInfo variety = maizeVarietyService.getVarietyByCode(varietyCode);

        return ResponseEntity.ok(variety);
    }

    @PostMapping
    @Operation(summary = "Create new variety",
            description = "Create a new maize variety")
    public ResponseEntity<VarietyInfo> createVariety(@Valid @RequestBody MaizeVariety variety) {
        log.info("Creating new maize variety: {}", variety.getVarietyName());

        VarietyInfo createdVariety = maizeVarietyService.createVariety(variety);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdVariety);
    }

    @PutMapping("/{varietyId}")
    @Operation(summary = "Update variety",
            description = "Update an existing maize variety")
    public ResponseEntity<VarietyInfo> updateVariety(
            @PathVariable Integer varietyId,
            @Valid @RequestBody MaizeVariety variety) {

        log.info("Updating variety with ID: {}", varietyId);

        VarietyInfo updatedVariety = maizeVarietyService.updateVariety(varietyId, variety);

        return ResponseEntity.ok(updatedVariety);
    }

    @DeleteMapping("/{varietyId}")
    @Operation(summary = "Delete variety",
            description = "Soft delete a maize variety (sets isActive to false)")
    public ResponseEntity<Void> deleteVariety(@PathVariable Integer varietyId) {
        log.info("Deleting variety with ID: {}", varietyId);

        maizeVarietyService.deleteVariety(varietyId);

        return ResponseEntity.noContent().build();
    }
}
