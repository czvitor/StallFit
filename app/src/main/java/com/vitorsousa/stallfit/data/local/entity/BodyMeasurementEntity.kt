package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * One row per "Avaliação Física" (assessment) — a history table, unlike the singleton
 * [UserProfileEntity]. [createdAt] is the date the assessment was performed (user-editable, not
 * necessarily "now"), and drives ordering in the Evolução Física history.
 *
 * [weightKg]/[heightCm] on the most recent row are what [com.vitorsousa.stallfit.domain.model.MetabolicCalculator]
 * now feeds on, replacing the fields that used to live directly on the profile.
 */
@Serializable
@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long,
    val weightKg: Double,
    val heightCm: Double,
    // Perímetros / circunferências (cm)
    val chestCm: Double? = null,
    val waistCm: Double? = null,
    val abdomenCm: Double? = null,
    val hipCm: Double? = null,
    val armLeftRelaxedCm: Double? = null,
    val armLeftFlexedCm: Double? = null,
    val armRightRelaxedCm: Double? = null,
    val armRightFlexedCm: Double? = null,
    val forearmLeftCm: Double? = null,
    val forearmRightCm: Double? = null,
    val thighLeftCm: Double? = null,
    val thighRightCm: Double? = null,
    val calfLeftCm: Double? = null,
    val calfRightCm: Double? = null,
    // Bioimpedância / composição corporal
    val bodyFatPercent: Double? = null,
    val leanMassKg: Double? = null,
    val fatMassKg: Double? = null,
    val bodyWaterPercent: Double? = null,
    // Dobras cutâneas (mm) — opcional
    val tricepsFoldMm: Double? = null,
    val subscapularFoldMm: Double? = null,
    val suprailiacFoldMm: Double? = null,
    val abdominalFoldMm: Double? = null,
    val thighFoldMm: Double? = null,
    val chestFoldMm: Double? = null,
    val midaxillaryFoldMm: Double? = null
)
