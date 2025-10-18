package org.project.agricultural_planting_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.dto.PlantingRequest;
import org.project.agricultural_planting_system.dto.PlantingResponse;
import org.project.agricultural_planting_system.service.PlantingCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planting")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Planting Calculations", description = "APIs for planting calculations and records")
@CrossOrigin(origins = "*")
public class PlantingController {

    private final PlantingCalculationService plantingCalculationService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate planting requirements",
            description = "Calculate seed, fertilizer, costs, and yield predictions for a given land")
    public ResponseEntity<PlantingResponse> calculatePlantingRequirements(
            @Valid @RequestBody PlantingRequest request) {

        log.info("Received planting calculation request for variety ID: {}", request.getVarietyId());

        PlantingResponse response = plantingCalculationService.calculatePlantingRequirements(request);

        log.info("Planting calculation completed successfully. Record ID: {}", response.getRecordId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/records")
    @Operation(summary = "Get all planting records",
            description = "Retrieve all historical planting records")
    public ResponseEntity<List<PlantingResponse>> getAllPlantingRecords() {
        log.info("Fetching all planting records");

        List<PlantingResponse> records = plantingCalculationService.getAllPlantingRecords();

        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "Get planting record by ID",
            description = "Retrieve a specific planting record by its ID")
    public ResponseEntity<PlantingResponse> getPlantingRecordById(
            @PathVariable Integer recordId) {

        log.info("Fetching planting record with ID: {}", recordId);

        PlantingResponse record = plantingCalculationService.getPlantingRecordById(recordId);

        return ResponseEntity.ok(record);
    }
}

