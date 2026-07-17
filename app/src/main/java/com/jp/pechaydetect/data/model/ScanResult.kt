package com.jp.pechaydetect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the result of a pechay leaf nutrient deficiency scan.
 * Stored locally in Room database for scan history.
 */
@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** Absolute file path of the captured/selected image. */
    val imagePath: String,
    /** Predicted class: Healthy | Nitrogen | Phosphorus | Potassium */
    val predictedClass: String,
    /** Confidence score (0.0–1.0) for the predicted class. */
    val confidence: Float,
    /** Whether the prediction confidence meets the OOD threshold (>= 0.70). */
    val isConfident: Boolean,
    /** Unix timestamp (ms) of when the scan was performed. */
    val timestamp: Long = System.currentTimeMillis(),
    /** Optional note added by the user. */
    val userNote: String = ""
)
