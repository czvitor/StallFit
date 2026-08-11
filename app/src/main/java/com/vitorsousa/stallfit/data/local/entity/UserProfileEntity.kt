package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Single-row table: the app tracks one user profile at a time, always stored at [id] = 1. */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val ageYears: Int,
    val weightKg: Double,
    val heightCm: Double,
    val sex: BiologicalSex,
    val activityLevel: ActivityLevel,
    val goal: NutritionGoal,
    val armCm: Double? = null,
    val chestCm: Double? = null,
    val hipCm: Double? = null,
    val thighCm: Double? = null,
    val calfCm: Double? = null
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
