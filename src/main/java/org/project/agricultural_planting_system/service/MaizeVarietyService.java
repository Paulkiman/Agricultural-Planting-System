package org.project.agricultural_planting_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.dto.VarietyInfo;
import org.project.agricultural_planting_system.model.MaizeVariety;
import org.project.agricultural_planting_system.repository.MaizeVarietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MaizeVarietyService {

    private final MaizeVarietyRepository varietyRepository;

    @Transactional(readOnly = true)
    public List<VarietyInfo> getAllActiveVarieties() {
        log.info("Fetching all active maize varieties");
        return varietyRepository.findAllActiveVarietiesOrdered()
                .stream()
                .map(this::convertToVarietyInfo)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VarietyInfo getVarietyById(Integer varietyId) {
        log.info("Fetching variety with ID: {}", varietyId);
        MaizeVariety variety = varietyRepository.findById(varietyId)
                .orElseThrow(() -> new RuntimeException("Variety not found with ID: " + varietyId));
        return convertToVarietyInfo(variety);
    }

    @Transactional(readOnly = true)
    public VarietyInfo getVarietyByCode(String varietyCode) {
        log.info("Fetching variety with code: {}", varietyCode);
        MaizeVariety variety = varietyRepository.findByVarietyCode(varietyCode)
                .orElseThrow(() -> new RuntimeException("Variety not found with code: " + varietyCode));
        return convertToVarietyInfo(variety);
    }

    public VarietyInfo createVariety(MaizeVariety variety) {
        log.info("Creating new maize variety: {}", variety.getVarietyName());

        if (varietyRepository.existsByVarietyName(variety.getVarietyName())) {
            throw new RuntimeException("Variety already exists with name: " + variety.getVarietyName());
        }

        if (varietyRepository.existsByVarietyCode(variety.getVarietyCode())) {
            throw new RuntimeException("Variety already exists with code: " + variety.getVarietyCode());
        }

        MaizeVariety savedVariety = varietyRepository.save(variety);
        return convertToVarietyInfo(savedVariety);
    }

    public VarietyInfo updateVariety(Integer varietyId, MaizeVariety updatedVariety) {
        log.info("Updating variety with ID: {}", varietyId);

        MaizeVariety existingVariety = varietyRepository.findById(varietyId)
                .orElseThrow(() -> new RuntimeException("Variety not found with ID: " + varietyId));

        existingVariety.setVarietyName(updatedVariety.getVarietyName());
        existingVariety.setSeedPricePerKg(updatedVariety.getSeedPricePerKg());
        existingVariety.setAverageCobMassKg(updatedVariety.getAverageCobMassKg());
        existingVariety.setExpectedCropsPerAcre(updatedVariety.getExpectedCropsPerAcre());
        existingVariety.setMaturityPeriodDays(updatedVariety.getMaturityPeriodDays());
        existingVariety.setDescription(updatedVariety.getDescription());
        existingVariety.setIsActive(updatedVariety.getIsActive());

        MaizeVariety savedVariety = varietyRepository.save(existingVariety);
        return convertToVarietyInfo(savedVariety);
    }

    public void deleteVariety(Integer varietyId) {
        log.info("Deleting variety with ID: {}", varietyId);

        MaizeVariety variety = varietyRepository.findById(varietyId)
                .orElseThrow(() -> new RuntimeException("Variety not found with ID: " + varietyId));

        // Soft delete by setting isActive to false
        variety.setIsActive(false);
        varietyRepository.save(variety);
    }

    private VarietyInfo convertToVarietyInfo(MaizeVariety variety) {
        return VarietyInfo.builder()
                .varietyId(variety.getVarietyId())
                .varietyName(variety.getVarietyName())
                .varietyCode(variety.getVarietyCode())
                .seedPricePerKg(variety.getSeedPricePerKg())
                .averageCobMassKg(variety.getAverageCobMassKg())
                .expectedCropsPerAcre(variety.getExpectedCropsPerAcre())
                .maturityPeriodDays(variety.getMaturityPeriodDays())
                .description(variety.getDescription())
                .build();
    }
}
