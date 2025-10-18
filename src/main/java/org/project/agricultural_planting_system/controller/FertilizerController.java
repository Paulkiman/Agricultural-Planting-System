package org.project.agricultural_planting_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.model.Fertilizer;
import org.project.agricultural_planting_system.service.FertilizerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fertilizers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Fertilizers", description = "APIs for managing fertilizers")
@CrossOrigin(origins = "*")
public class FertilizerController {

    private final FertilizerService fertilizerService;

    @GetMapping
    @Operation(summary = "Get all active fertilizers",
            description = "Retrieve all active fertilizers")
    public ResponseEntity<List<Fertilizer>> getAllActiveFertilizers() {
        log.info("Fetching all active fertilizers");

        List<Fertilizer> fertilizers = fertilizerService.getAllActiveFertilizers();

        return ResponseEntity.ok(fertilizers);
    }

    @GetMapping("/{fertilizerId}")
    @Operation(summary = "Get fertilizer by ID",
            description = "Retrieve a specific fertilizer by its ID")
    public ResponseEntity<Fertilizer> getFertilizerById(@PathVariable Integer fertilizerId) {
        log.info("Fetching fertilizer with ID: {}", fertilizerId);

        Fertilizer fertilizer = fertilizerService.getFertilizerById(fertilizerId);

        return ResponseEntity.ok(fertilizer);
    }

    @GetMapping("/type/{fertilizerType}")
    @Operation(summary = "Get fertilizer by type",
            description = "Retrieve a specific fertilizer by its type")
    public ResponseEntity<Fertilizer> getFertilizerByType(@PathVariable String fertilizerType) {
        log.info("Fetching fertilizer with type: {}", fertilizerType);

        Fertilizer fertilizer = fertilizerService.getFertilizerByType(fertilizerType);

        return ResponseEntity.ok(fertilizer);
    }

    @PostMapping
    @Operation(summary = "Create new fertilizer",
            description = "Create a new fertilizer")
    public ResponseEntity<Fertilizer> createFertilizer(@Valid @RequestBody Fertilizer fertilizer) {
        log.info("Creating new fertilizer: {}", fertilizer.getFertilizerType());

        Fertilizer createdFertilizer = fertilizerService.createFertilizer(fertilizer);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdFertilizer);
    }

    @PutMapping("/{fertilizerId}")
    @Operation(summary = "Update fertilizer",
            description = "Update an existing fertilizer")
    public ResponseEntity<Fertilizer> updateFertilizer(
            @PathVariable Integer fertilizerId,
            @Valid @RequestBody Fertilizer fertilizer) {

        log.info("Updating fertilizer with ID: {}", fertilizerId);

        Fertilizer updatedFertilizer = fertilizerService.updateFertilizer(fertilizerId, fertilizer);

        return ResponseEntity.ok(updatedFertilizer);
    }

    @DeleteMapping("/{fertilizerId}")
    @Operation(summary = "Delete fertilizer",
            description = "Soft delete a fertilizer (sets isActive to false)")
    public ResponseEntity<Void> deleteFertilizer(@PathVariable Integer fertilizerId) {
        log.info("Deleting fertilizer with ID: {}", fertilizerId);

        fertilizerService.deleteFertilizer(fertilizerId);

        return ResponseEntity.noContent().build();
    }
}
