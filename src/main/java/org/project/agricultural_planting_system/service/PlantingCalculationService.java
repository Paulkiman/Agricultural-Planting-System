package org.project.agricultural_planting_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.agricultural_planting_system.dto.*;
import org.project.agricultural_planting_system.model.MaizeVariety;
import org.project.agricultural_planting_system.model.PlantingParameters;
import org.project.agricultural_planting_system.model.PlantingRecord;
import org.project.agricultural_planting_system.model.VarietyFertilizer;
import org.project.agricultural_planting_system.repository.MaizeVarietyRepository;
import org.project.agricultural_planting_system.repository.PlantingParametersRepository;
import org.project.agricultural_planting_system.repository.PlantingRecordRepository;
import org.project.agricultural_planting_system.repository.VarietyFertilizerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlantingCalculationService {

    private final MaizeVarietyRepository varietyRepository;
    private final PlantingParametersRepository parametersRepository;
    private final VarietyFertilizerRepository varietyFertilizerRepository;
    private final PlantingRecordRepository plantingRecordRepository;

    @Value("${app.planting.default-seed-spacing-cm:30.0}")
    private BigDecimal defaultSeedSpacingCm;

    @Value("${app.planting.default-line-spacing-cm:70.0}")
    private BigDecimal defaultLineSpacingCm;

    @Value("${app.planting.package-weight-kg:90.0}")
    private BigDecimal packageWeightKg;

    @Value("${app.planting.sqm-per-acre:4046.86}")
    private BigDecimal sqmPerAcre;

    /**
     * Main calculation method - processes planting request and returns complete response
     */
    public PlantingResponse calculatePlantingRequirements(PlantingRequest request) {
        log.info("Processing planting calculation for variety ID: {}", request.getVarietyId());

        // 1. Fetch variety information
        MaizeVariety variety = varietyRepository.findById(request.getVarietyId())
                .orElseThrow(() -> new RuntimeException("Variety not found with ID: " + request.getVarietyId()));

        if (!variety.getIsActive()) {
            throw new RuntimeException("Variety is not active: " + variety.getVarietyName());
        }

        // 2. Get planting parameters
        PlantingParameters parameters = parametersRepository.findByIsDefaultTrue()
                .orElseGet(() -> createDefaultParameters());

        // 3. Calculate land information
        LandInfo landInfo = calculateLandInfo(
                request.getLandLengthMeters(),
                request.getLandWidthMeters(),
                parameters
        );

        // 4. Calculate seed requirements
        SeedCalculation seedCalculation = calculateSeedRequirements(
                landInfo,
                variety.getSeedPricePerKg()
        );

        // 5. Calculate fertilizer requirements
        FertilizerCalculation fertilizerCalculation = calculateFertilizerRequirements(
                seedCalculation.getTotalSeedsRequired(),
                request.getDapPricePerKg(),
                request.getNpkPricePerKg(),
                variety.getVarietyId()
        );

        // 6. Calculate cost breakdown
        CostBreakdown costBreakdown = calculateCostBreakdown(
                seedCalculation,
                fertilizerCalculation,
                landInfo
        );

        // 7. Calculate yield prediction
        YieldPrediction yieldPrediction = calculateYieldPrediction(
                variety,
                landInfo,
                request.getMarketPricePerPackage(),
                costBreakdown.getTotalInputCost()
        );

        // 8. Calculate expected harvest date
        LocalDate expectedHarvestDate = null;
        if (request.getPlantingDate() != null) {
            expectedHarvestDate = request.getPlantingDate()
                    .plusDays(variety.getMaturityPeriodDays());
        }

        // 9. Save planting record
        PlantingRecord record = savePlantingRecord(
                request,
                variety,
                landInfo,
                seedCalculation,
                fertilizerCalculation,
                costBreakdown,
                yieldPrediction,
                expectedHarvestDate
        );

        // 10. Build and return response
        return PlantingResponse.builder()
                .recordId(record.getRecordId())
                .varietyInfo(buildVarietyInfo(variety))
                .landInfo(landInfo)
                .seedCalculation(seedCalculation)
                .fertilizerCalculation(fertilizerCalculation)
                .costBreakdown(costBreakdown)
                .yieldPrediction(yieldPrediction)
                .farmerName(request.getFarmerName())
                .farmLocation(request.getFarmLocation())
                .plantingDate(request.getPlantingDate())
                .expectedHarvestDate(expectedHarvestDate)
                .notes(request.getNotes())
                .calculatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Calculate land information
     */
    private LandInfo calculateLandInfo(
            BigDecimal lengthMeters,
            BigDecimal widthMeters,
            PlantingParameters parameters) {

        BigDecimal areaSqm = lengthMeters.multiply(widthMeters)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal areaAcres = areaSqm.divide(sqmPerAcre, 4, RoundingMode.HALF_UP);

        return LandInfo.builder()
                .lengthMeters(lengthMeters)
                .widthMeters(widthMeters)
                .areaSqm(areaSqm)
                .areaAcres(areaAcres)
                .seedSpacingCm(parameters.getSeedSpacingCm())
                .lineSpacingCm(parameters.getLineSpacingCm())
                .build();
    }

    /**
     * Calculate seed requirements
     */
    private SeedCalculation calculateSeedRequirements(
            LandInfo landInfo,
            BigDecimal seedPricePerKg) {

        // Convert land dimensions to centimeters
        BigDecimal lengthCm = landInfo.getLengthMeters().multiply(new BigDecimal("100"));
        BigDecimal widthCm = landInfo.getWidthMeters().multiply(new BigDecimal("100"));

        // Calculate number of seeds per row and number of rows
        int seedsPerRow = lengthCm.divide(landInfo.getSeedSpacingCm(), 0, RoundingMode.DOWN).intValue();
        int numberOfRows = widthCm.divide(landInfo.getLineSpacingCm(), 0, RoundingMode.DOWN).intValue();

        // Total seeds required
        int totalSeeds = seedsPerRow * numberOfRows;

        // Assuming 25000 seeds per kg (standard for maize)
        BigDecimal seedsPerKg = new BigDecimal("25000");
        BigDecimal seedWeightKg = new BigDecimal(totalSeeds)
                .divide(seedsPerKg, 3, RoundingMode.HALF_UP);

        // Calculate seed cost
        BigDecimal seedCost = seedWeightKg.multiply(seedPricePerKg)
                .setScale(2, RoundingMode.HALF_UP);

        return SeedCalculation.builder()
                .totalSeedsRequired(totalSeeds)
                .seedWeightKg(seedWeightKg)
                .seedCost(seedCost)
                .seedsPerRow(seedsPerRow)
                .numberOfRows(numberOfRows)
                .build();
    }

    /**
     * Calculate fertilizer requirements
     */
    private FertilizerCalculation calculateFertilizerRequirements(
            Integer totalSeeds,
            BigDecimal dapPricePerKg,
            BigDecimal npkPricePerKg,
            Integer varietyId) {

        // Get variety-specific fertilizer requirements or use defaults
        List<VarietyFertilizer> varietyFertilizers =
                varietyFertilizerRepository.findAllByVarietyIdWithFertilizers(varietyId);

        // Default values if not found in variety_fertilizer table
        BigDecimal dapGramsPerSeed = new BigDecimal("2.3");
        BigDecimal npkGramsPerSeed = new BigDecimal("5.6");

        // Override with variety-specific values if available
        for (VarietyFertilizer vf : varietyFertilizers) {
            if ("DAP".equalsIgnoreCase(vf.getFertilizer().getFertilizerType())) {
                dapGramsPerSeed = vf.getRequiredAmountGrams();
            } else if ("NPK".equalsIgnoreCase(vf.getFertilizer().getFertilizerType())) {
                npkGramsPerSeed = vf.getRequiredAmountGrams();
            }
        }

        // Calculate DAP requirements
        BigDecimal dapTotalGrams = dapGramsPerSeed.multiply(new BigDecimal(totalSeeds));
        BigDecimal dapWeightKg = dapTotalGrams.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_UP);
        BigDecimal dapCost = dapWeightKg.multiply(dapPricePerKg).setScale(2, RoundingMode.HALF_UP);

        FertilizerDetail dapDetail = FertilizerDetail.builder()
                .fertilizerType("DAP")
                .gramsPerSeed(dapGramsPerSeed)
                .totalWeightKg(dapWeightKg)
                .pricePerKg(dapPricePerKg)
                .totalCost(dapCost)
                .build();

        // Calculate NPK requirements
        BigDecimal npkTotalGrams = npkGramsPerSeed.multiply(new BigDecimal(totalSeeds));
        BigDecimal npkWeightKg = npkTotalGrams.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_UP);
        BigDecimal npkCost = npkWeightKg.multiply(npkPricePerKg).setScale(2, RoundingMode.HALF_UP);

        FertilizerDetail npkDetail = FertilizerDetail.builder()
                .fertilizerType("NPK")
                .gramsPerSeed(npkGramsPerSeed)
                .totalWeightKg(npkWeightKg)
                .pricePerKg(npkPricePerKg)
                .totalCost(npkCost)
                .build();

        // Total fertilizer
        BigDecimal totalFertilizerWeight = dapWeightKg.add(npkWeightKg)
                .setScale(3, RoundingMode.HALF_UP);
        BigDecimal totalFertilizerCost = dapCost.add(npkCost)
                .setScale(2, RoundingMode.HALF_UP);

        return FertilizerCalculation.builder()
                .dap(dapDetail)
                .npk(npkDetail)
                .totalFertilizerWeightKg(totalFertilizerWeight)
                .totalFertilizerCost(totalFertilizerCost)
                .build();
    }

    /**
     * Calculate cost breakdown
     */
    private CostBreakdown calculateCostBreakdown(
            SeedCalculation seedCalculation,
            FertilizerCalculation fertilizerCalculation,
            LandInfo landInfo) {

        BigDecimal totalInputCost = seedCalculation.getSeedCost()
                .add(fertilizerCalculation.getTotalFertilizerCost())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal costPerAcre = totalInputCost
                .divide(landInfo.getAreaAcres(), 2, RoundingMode.HALF_UP);

        BigDecimal costPerSqm = totalInputCost
                .divide(landInfo.getAreaSqm(), 4, RoundingMode.HALF_UP);

        return CostBreakdown.builder()
                .seedCost(seedCalculation.getSeedCost())
                .dapCost(fertilizerCalculation.getDap().getTotalCost())
                .npkCost(fertilizerCalculation.getNpk().getTotalCost())
                .totalFertilizerCost(fertilizerCalculation.getTotalFertilizerCost())
                .totalInputCost(totalInputCost)
                .costPerAcre(costPerAcre)
                .costPerSqm(costPerSqm)
                .build();
    }

    /**
     * Calculate yield prediction
     */
    private YieldPrediction calculateYieldPrediction(
            MaizeVariety variety,
            LandInfo landInfo,
            BigDecimal marketPricePerPackage,
            BigDecimal totalInputCost) {

        // Calculate expected yield based on variety and land area
        BigDecimal expectedCropsTotal = new BigDecimal(variety.getExpectedCropsPerAcre())
                .multiply(landInfo.getAreaAcres())
                .setScale(0, RoundingMode.HALF_UP);

        BigDecimal expectedYieldKg = variety.getAverageCobMassKg()
                .multiply(expectedCropsTotal)
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate number of 90kg packages
        int expectedPackages = expectedYieldKg
                .divide(packageWeightKg, 0, RoundingMode.DOWN)
                .intValue();

        // Calculate revenue
        BigDecimal predictedRevenue = new BigDecimal(expectedPackages)
                .multiply(marketPricePerPackage)
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate profit margin
        BigDecimal profitMargin = predictedRevenue.subtract(totalInputCost)
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate profit margin percentage
        BigDecimal profitMarginPercentage = BigDecimal.ZERO;
        if (totalInputCost.compareTo(BigDecimal.ZERO) > 0) {
            profitMarginPercentage = profitMargin
                    .divide(totalInputCost, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // Revenue per acre
        BigDecimal revenuePerAcre = predictedRevenue
                .divide(landInfo.getAreaAcres(), 2, RoundingMode.HALF_UP);

        // ROI calculation
        BigDecimal roi = BigDecimal.ZERO;
        if (totalInputCost.compareTo(BigDecimal.ZERO) > 0) {
            roi = profitMargin
                    .divide(totalInputCost, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return YieldPrediction.builder()
                .expectedYieldKg(expectedYieldKg)
                .expectedPackages90kg(expectedPackages)
                .marketPricePerPackage(marketPricePerPackage)
                .predictedRevenue(predictedRevenue)
                .profitMargin(profitMargin)
                .profitMarginPercentage(profitMarginPercentage)
                .revenuePerAcre(revenuePerAcre)
                .returnOnInvestment(roi)
                .build();
    }

    /**
     * Save planting record to database
     */
    private PlantingRecord savePlantingRecord(
            PlantingRequest request,
            MaizeVariety variety,
            LandInfo landInfo,
            SeedCalculation seedCalculation,
            FertilizerCalculation fertilizerCalculation,
            CostBreakdown costBreakdown,
            YieldPrediction yieldPrediction,
            LocalDate expectedHarvestDate) {

        PlantingRecord record = PlantingRecord.builder()
                .variety(variety)
                .farmerName(request.getFarmerName())
                .farmLocation(request.getFarmLocation())
                .landLengthMeters(request.getLandLengthMeters())
                .landWidthMeters(request.getLandWidthMeters())
                .landAreaSqm(landInfo.getAreaSqm())
                .landAreaAcres(landInfo.getAreaAcres())
                .totalSeedsRequired(seedCalculation.getTotalSeedsRequired())
                .seedWeightKg(seedCalculation.getSeedWeightKg())
                .seedCost(seedCalculation.getSeedCost())
                .dapWeightKg(fertilizerCalculation.getDap().getTotalWeightKg())
                .dapCost(fertilizerCalculation.getDap().getTotalCost())
                .npkWeightKg(fertilizerCalculation.getNpk().getTotalWeightKg())
                .npkCost(fertilizerCalculation.getNpk().getTotalCost())
                .totalFertilizerWeightKg(fertilizerCalculation.getTotalFertilizerWeightKg())
                .totalInputCost(costBreakdown.getTotalInputCost())
                .expectedYieldKg(yieldPrediction.getExpectedYieldKg())
                .expectedPackages90kg(yieldPrediction.getExpectedPackages90kg())
                .marketPricePerPackage(yieldPrediction.getMarketPricePerPackage())
                .predictedRevenue(yieldPrediction.getPredictedRevenue())
                .profitMargin(yieldPrediction.getProfitMargin())
                .plantingDate(request.getPlantingDate())
                .expectedHarvestDate(expectedHarvestDate)
                .notes(request.getNotes())
                .build();

        return plantingRecordRepository.save(record);
    }

    /**
     * Build variety info DTO
     */
    private VarietyInfo buildVarietyInfo(MaizeVariety variety) {
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

    /**
     * Create default planting parameters if none exist
     */
    private PlantingParameters createDefaultParameters() {
        PlantingParameters params = PlantingParameters.builder()
                .seedSpacingCm(defaultSeedSpacingCm)
                .lineSpacingCm(defaultLineSpacingCm)
                .parameterName("System Default")
                .description("System generated default parameters")
                .isDefault(true)
                .isActive(true)
                .build();

        return parametersRepository.save(params);
    }

    /**
     * Get all planting records
     */
    @Transactional(readOnly = true)
    public List<PlantingResponse> getAllPlantingRecords() {
        return plantingRecordRepository.findAllWithVarietyOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get planting record by ID
     */
    @Transactional(readOnly = true)
    public PlantingResponse getPlantingRecordById(Integer recordId) {
        PlantingRecord record = plantingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found with ID: " + recordId));

        return convertToResponse(record);
    }

    /**
     * Convert PlantingRecord entity to PlantingResponse DTO
     */
    private PlantingResponse convertToResponse(PlantingRecord record) {
        MaizeVariety variety = record.getVariety();

        return PlantingResponse.builder()
                .recordId(record.getRecordId())
                .varietyInfo(buildVarietyInfo(variety))
                .landInfo(LandInfo.builder()
                        .lengthMeters(record.getLandLengthMeters())
                        .widthMeters(record.getLandWidthMeters())
                        .areaSqm(record.getLandAreaSqm())
                        .areaAcres(record.getLandAreaAcres())
                        .build())
                .seedCalculation(SeedCalculation.builder()
                        .totalSeedsRequired(record.getTotalSeedsRequired())
                        .seedWeightKg(record.getSeedWeightKg())
                        .seedCost(record.getSeedCost())
                        .build())
                .fertilizerCalculation(FertilizerCalculation.builder()
                        .dap(FertilizerDetail.builder()
                                .fertilizerType("DAP")
                                .totalWeightKg(record.getDapWeightKg())
                                .totalCost(record.getDapCost())
                                .build())
                        .npk(FertilizerDetail.builder()
                                .fertilizerType("NPK")
                                .totalWeightKg(record.getNpkWeightKg())
                                .totalCost(record.getNpkCost())
                                .build())
                        .totalFertilizerWeightKg(record.getTotalFertilizerWeightKg())
                        .build())
                .costBreakdown(CostBreakdown.builder()
                        .seedCost(record.getSeedCost())
                        .dapCost(record.getDapCost())
                        .npkCost(record.getNpkCost())
                        .totalInputCost(record.getTotalInputCost())
                        .build())
                .yieldPrediction(YieldPrediction.builder()
                        .expectedYieldKg(record.getExpectedYieldKg())
                        .expectedPackages90kg(record.getExpectedPackages90kg())
                        .marketPricePerPackage(record.getMarketPricePerPackage())
                        .predictedRevenue(record.getPredictedRevenue())
                        .profitMargin(record.getProfitMargin())
                        .build())
                .farmerName(record.getFarmerName())
                .farmLocation(record.getFarmLocation())
                .plantingDate(record.getPlantingDate())
                .expectedHarvestDate(record.getExpectedHarvestDate())
                .notes(record.getNotes())
                .calculatedAt(record.getCreatedAt())
                .build();
    }
}
