package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Single-row table: the app tracks one user profile at a time, always stored at [id] = 1.
 * Holds only fixed/general data — weight, height and body measurements now live in
 * [BodyMeasurementEntity]'s history instead, since those change over time.
 */
@Serializable
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val name: String = "",
    val ageYears: Int,
    val sex: BiologicalSex,
    val activityLevel: ActivityLevel,
    val goal: NutritionGoal
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
