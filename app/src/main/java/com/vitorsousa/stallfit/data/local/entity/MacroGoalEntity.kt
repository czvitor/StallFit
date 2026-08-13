package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/** Single-row table: the app has one active macro goal at a time, always stored at [id] = 1. */
@Serializable
@Entity(tableName = "macro_goal")
data class MacroGoalEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val calorieGoal: Int,
    val proteinGoal: Int,
    val carbGoal: Int,
    val fatGoal: Int
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
