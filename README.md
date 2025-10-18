Agricultural-Planting-System
Spring Boot application for calculating maize planting resources, fertilizer requirements, and yield predictions. Helps farmers optimize input costs.

Agricultural Planting System

A comprehensive Spring Boot application designed to help farmers optimize their maize planting operations through accurate resource calculations and yield predictions. Overview The Agricultural Planting System is a web-based application that integrates with modern seed planting machinery to provide farmers with data-driven insights for maize cultivation. The system helps farmers determine the exact quantities of seeds and fertilizers needed, calculate input costs, and predict expected yields and revenue. Key Features ✅ Seed Calculation - Precise seed requirements based on land dimensions (30cm seed spacing, 70cm line spacing) ✅ Fertilizer Computation - Automated DAP (2.3g/seed) and NPK (5.6g/seed) calculations ✅ Cost Analysis - Real-time input cost estimation with variable pricing ✅ Yield Prediction - Revenue forecasting using variety-specific crop data ✅ Data Persistence - Historical calculation tracking via MySQL database ✅ REST API - Well-documented endpoints with Swagger UI

🚀 Quick Start Prerequisites

Java 17+ MySQL 8.0+ Gradle 7.0+

How It Works Input Farmer provides:

Land dimensions (length × width in meters) Current market prices (seeds, DAP, NPK) Selected maize variety

Processing System calculates:

Seeds needed = Land area ÷ (30cm × 70cm) DAP required = Seeds × 2.3g ÷ 1000 NPK required = Seeds × 5.6g ÷ 1000 Total cost = (Seed + DAP + NPK) costs Predicted yield = Variety data × expected crops

Output

Detailed resource breakdown (kg) Individual and total costs Expected harvest (packages & revenue) Profit margin estimation

🏗️ Technology Stack CategoryTechnologyBackendSpring Boot 3.5.6, Java 17DatabaseMySQL 8.0, Hibernate/JPAAPI DocsSpringDoc OpenAPI 2.7.0Build ToolGradleTestingJUnit 5, Mockito

📊 Database Schema Core Tables maize_variety - Stores variety information and characteristics sqlvariety_id, variety_name, seed_price_per_kg, avg_cob_mass_kg, expected_crops_acre fertilizer - Maintains fertilizer types and pricing sqlfertilizer_id, fertilizer_type, price_per_kg, amount_per_seed_grams calculation_history - Tracks all farmer calculations sqlcalculation_id, land_dimensions, seed_data, fertilizer_data, cost_analysis, yield_prediction Full schema: View Database Design

🔌 API Endpoints Calculate Planting Requirements httpPOST /api/planting/calculate Content-Type: application/json

{ "length": 100.0, "width": 50.0, "varietyId": 1, "seedPricePerKg": 450.00, "dapPricePerKg": 85.00, "npkPricePerKg": 92.00, "marketPricePerPackage": 4000.00 } Response: json{ "totalSeeds": 23810, "seedWeightKg": 952.4, "dapWeightKg": 54.76, "npkWeightKg": 133.34, "totalInputCost": 445402.88, "predictedPackages": 204, "predictedRevenue": 816000.00 } Full API documentation: Available at /swagger-ui.html

