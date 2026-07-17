package com.jp.pechaydetect.data.model

/**
 * Describes educational information about a nutrient and its role
 * in pechay growth, as presented in the Nutrient Library screen.
 */
data class NutrientInfo(
    val nutrientName: String,
    val symbol: String,
    val role: String,
    val deficiencySymptoms: String,
    val affectedParts: String,
    val colorIconRes: Int
)
