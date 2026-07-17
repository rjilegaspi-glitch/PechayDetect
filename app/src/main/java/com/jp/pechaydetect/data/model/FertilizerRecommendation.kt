package com.jp.pechaydetect.data.model

/**
 * A fertilizer recommendation step for a specific nutrient deficiency,
 * sourced from the Philippine Department of Agriculture (DA).
 */
data class FertilizerRecommendation(
    /** Target deficiency class (matches classifier output). */
    val deficiencyClass: String,
    /** Short summary title shown on the card. */
    val title: String,
    /** Severity description. */
    val severity: String,
    /** List of recommended fertilizers with dosage and timing. */
    val recommendations: List<FertilizerStep>,
    /** Cultural management practices to complement fertilization. */
    val culturalPractices: List<String>,
    /** Safety and environmental notes from the DA. */
    val notes: String,
    /** Reference to the DA guideline. */
    val daReference: String
)

/**
 * A single fertilizer application step.
 */
data class FertilizerStep(
    /** Fertilizer product name (e.g., "Urea 46-0-0"). */
    val product: String,
    /** Application rate (e.g., "5–7 g/sqm"). */
    val rate: String,
    /** Application method (e.g., "Side dress, 3 cm from base"). */
    val method: String,
    /** Timing relative to transplanting (e.g., "10–14 days after transplanting"). */
    val timing: String,
    /** Whether this product is organically certified. */
    val isOrganic: Boolean = false
)
