package com.vitorsousa.stallfit.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** [dateEpochDay] is `LocalDate.toEpochDay()` — a stable, timezone-safe day bucket for diary queries. */
@Entity(
    tableName = "meal_entries",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("foodId"), Index("dateEpochDay")]
)
data class MealEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodId: Long,
    val mealType: MealType,
    val grams: Double,
    val dateEpochDay: Long,
    val loggedAt: Long
)
