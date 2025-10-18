package org.project.agricultural_planting_system.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.model.Fertilizer;
import org.project.agricultural_planting_system.repository.FertilizerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FertilizerService {

    private final FertilizerRepository fertilizerRepository;

    @Transactional
    public List<Fertilizer> getAllActiveFertilizers() {
        log.info("Fetching all active fertilizers");
        return fertilizerRepository.findAllActiveFertilizersOrdered();
    }

    @Transactional
    public Fertilizer getFertilizerById(Integer fertilizerId) {
        log.info("Fetching fertilizer with ID: {}", fertilizerId);
        return fertilizerRepository.findById(fertilizerId)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with ID: " + fertilizerId));
    }

    @Transactional
    public Fertilizer getFertilizerByType(String fertilizerType) {
        log.info("Fetching fertilizer with type: {}", fertilizerType);
        return fertilizerRepository.findByFertilizerType(fertilizerType)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with type: " + fertilizerType));
    }

    public Fertilizer createFertilizer(Fertilizer fertilizer) {
        log.info("Creating new fertilizer: {}", fertilizer.getFertilizerType());

        if (fertilizerRepository.existsByFertilizerType(fertilizer.getFertilizerType())) {
            throw new RuntimeException("Fertilizer already exists with type: " + fertilizer.getFertilizerType());
        }

        return fertilizerRepository.save(fertilizer);
    }

    public Fertilizer updateFertilizer(Integer fertilizerId, Fertilizer updatedFertilizer) {
        log.info("Updating fertilizer with ID: {}", fertilizerId);

        Fertilizer existingFertilizer = fertilizerRepository.findById(fertilizerId)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with ID: " + fertilizerId));

        existingFertilizer.setFertilizerType(updatedFertilizer.getFertilizerType());
        existingFertilizer.setPricePerKg(updatedFertilizer.getPricePerKg());
        existingFertilizer.setAmountPerSeedGrams(updatedFertilizer.getAmountPerSeedGrams());
        existingFertilizer.setNutrientComposition(updatedFertilizer.getNutrientComposition());
        existingFertilizer.setDescription(updatedFertilizer.getDescription());
        existingFertilizer.setIsActive(updatedFertilizer.getIsActive());

        return fertilizerRepository.save(existingFertilizer);
    }

    public void deleteFertilizer(Integer fertilizerId) {
        log.info("Deleting fertilizer with ID: {}", fertilizerId);

        Fertilizer fertilizer = fertilizerRepository.findById(fertilizerId)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found with ID: " + fertilizerId));

        // Soft delete
        fertilizer.setIsActive(false);
        fertilizerRepository.save(fertilizer);
    }
}

